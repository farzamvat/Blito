package com.blito.services;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.blito.configs.Constants;
import com.blito.enums.BankGateway;
import com.blito.enums.PaymentStatus;
import com.blito.enums.Response;
import com.blito.enums.State;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.CommonBlitMapper;
import com.blito.models.BlitType;
import com.blito.models.CommonBlit;
import com.blito.models.Event;
import com.blito.models.User;
import com.blito.repositories.BlitRepository;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.CommonBlitRepository;
import com.blito.repositories.EventRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.payments.SamanPaymentRequestResponseViewModel;
import com.blito.rest.viewmodels.payments.ZarinpalPayRequetsResponseViewModel;
import com.blito.search.SearchViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.util.HtmlRenderer;

@Service
public class BlitService {

	@Autowired
	private CommonBlitMapper commonBlitMapper;
	@Autowired
	private CommonBlitRepository commonBlitRepository;
	@Autowired
	private BlitTypeRepository blitTypeRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BlitRepository blitRepository;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private ExcelService excelService;
	@Autowired
	private HtmlRenderer htmlRenderer;
	@Autowired
	private EventRepository eventRepository;
	
	@Value("${zarinpal.web.gateway}")
	private String zarinpalGatewayURL;
	
	private final Logger log = LoggerFactory.getLogger(BlitService.class);

	
	public String generateCommonBlitHtml(CommonBlit commonBllit)
	{
		// TODO
		return htmlRenderer.renderHtml("ticket", null);
	}
	
	public CompletableFuture<Object> createCommonBlit(CommonBlitViewModel vmodel) {

		CommonBlit commonBlit = commonBlitMapper.createFromViewModel(vmodel);
		BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(vmodel.getBlitTypeId()))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
		
		
		
		// ADDITIONAL FIELDS VALIDATION
		// if condition && instead of || ??
		if( !blitType.getEventDate().getEvent().getAdditionalFields().isEmpty() || blitType.getEventDate().getEvent().getAdditionalFields() != null)
		{
			// TODO exception messages
			Event event = blitType.getEventDate().getEvent();
			if(!event.getAdditionalFields().isEmpty())
				if(vmodel.getAdditionalFields().isEmpty() || vmodel.getAdditionalFields() == null)
					throw new RuntimeException("additional fields map is empty");
				vmodel.getAdditionalFields().forEach((k,v) -> {
					event.getAdditionalFields().keySet().stream().filter(key -> key == k).findFirst()
						.ifPresent(key -> {
							if(event.getAdditionalFields().get(key).equals(Constants.FIELD_DOUBLE_TYPE))
							{
								try {
									Double.parseDouble(v);
								} catch(Exception e) 
								{
									throw new RuntimeException(e.getMessage());
								}
							}
							else if(event.getAdditionalFields().get(key).equals(Constants.FIELD_INT_TYPE))
							{
								try {
									Integer.parseInt(v);
								} catch (Exception e)
								{
									throw new RuntimeException(e.getMessage());
								}
							}
						});
				});
		}
		// ADDITIONAL FIELDS VALIDATION

		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		
		if (blitType.isFree()) {
			return CompletableFuture
					.completedFuture(commonBlitMapper.createFromEntity(reserveFreeBlit(blitType, commonBlit, user)));
		} else {
			if (commonBlit.getCount() * blitType.getPrice() != commonBlit.getTotalAmount())
				throw new InconsistentDataException("total amount is not equal to price * count");
			return buyCommonBlit(blitType, commonBlit, user);
		}
	}

	private CompletableFuture<Object> buyCommonBlit(BlitType blitType, CommonBlit commonBlit, User user) {
		checkBlitTypeRestrictionsForBuy(blitType, commonBlit);
		String trackCode = generateTrackCode();
		switch (Enum.valueOf(BankGateway.class, commonBlit.getBankGateway())) {
		case ZARINPAL:
			return paymentService.zarinpalRequestToken((int)commonBlit.getTotalAmount(), user.getEmail(), user.getMobile(), blitType.getEventDate().getEvent().getDescription())
					.thenApply(token -> {
						CommonBlit persisted = persistNoneFreeCommonBlit(blitType, commonBlit, user, token, trackCode);
						ZarinpalPayRequetsResponseViewModel zarinpalResponse = new ZarinpalPayRequetsResponseViewModel();
						zarinpalResponse.setGateway(BankGateway.ZARINPAL);
						zarinpalResponse.setZarinpalWebGatewayURL(zarinpalGatewayURL + persisted.getToken());
						return zarinpalResponse;
					});
		case SAMAN:
			return paymentService.samanBankRequestToken(trackCode, commonBlit.getTotalAmount())
					.thenApply(token -> {
						CommonBlit persisted = persistNoneFreeCommonBlit(blitType, commonBlit, user, token, trackCode);
						SamanPaymentRequestResponseViewModel samanResponse = new SamanPaymentRequestResponseViewModel();
						samanResponse.setToken(persisted.getToken());
						samanResponse.setGateway(BankGateway.SAMAN);
						samanResponse.setRedirectURL("http://localhost:8085/ws");
						return samanResponse;
					});
		default:
			throw new NotFoundException(ResourceUtil.getMessage(Response.BANK_GATEWAY_NOT_FOUND));
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	private CommonBlit persistNoneFreeCommonBlit(BlitType blitType,CommonBlit commonBlit,User user,String token,String trackCode)
	{
		checkBlitTypeRestrictionsForBuy(blitType, commonBlit);
		BlitType attachedBlitType = blitTypeRepository.findOne(blitType.getBlitTypeId());
		User attachedUser = userRepository.findOne(user.getUserId());
		commonBlit.setUser(attachedUser);
		commonBlit.setBlitType(attachedBlitType);
		commonBlit.setToken(token);
		commonBlit.setTrackCode(trackCode);
		commonBlit.setPaymentStatus(PaymentStatus.PENDING.name());
		return commonBlitRepository.save(commonBlit);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	private CommonBlit reserveFreeBlit(BlitType blitType, CommonBlit commonBlit, User user) {
		checkBlitTypeRestrictionsForBuy(blitType, commonBlit);
		BlitType attachedBlitType = blitTypeRepository.findOne(blitType.getBlitTypeId());
		User attachedUser = userRepository.findOne(user.getUserId());
		attachedBlitType.setSoldCount(attachedBlitType.getSoldCount() + commonBlit.getCount());
		if(attachedBlitType.getSoldCount() == attachedBlitType.getCapacity())
			attachedBlitType.setBlitTypeState(State.SOLD.name());
		//TODO sold event dates and event
		commonBlit.setTrackCode(generateTrackCode());
		commonBlit.setUser(attachedUser);
		commonBlit.setBlitType(attachedBlitType);
		commonBlit.setPaymentStatus(PaymentStatus.FREE.name());
		commonBlit.setBankGateway(BankGateway.NONE.name());
		attachedUser.addBlits(commonBlit);
		//
		//
		//
		//
		// sending email asynchronously
		return commonBlitRepository.save(commonBlit);
	}

	private void checkBlitTypeRestrictionsForBuy(BlitType blitType, CommonBlit commonBlit) {
		blitType = blitTypeRepository.findOne(blitType.getBlitTypeId());

		if (blitType.getBlitTypeState().equals(State.SOLD))
			throw new RuntimeException("sold out");

		if (blitType.getBlitTypeState().equals(State.CLOSED))
			throw new RuntimeException("closed");
		if(!blitType.getEventDate().getEvent().getEventState().equals(State.OPEN.name()))
		{
			throw new RuntimeException("closed or sold out or ended");
		}
		if (commonBlit.getCount() + blitType.getSoldCount() > blitType.getCapacity())
			throw new InconsistentDataException("more than blit type capacity");
	}

	public String generateTrackCode() {
		String trackCode = RandomUtil.generateTrackCode();
		while (blitRepository.findByTrackCode(trackCode).isPresent()) {
			return generateTrackCode();
		}
		return trackCode;
	}
	
	public Page<CommonBlitViewModel> searchCommonBlits(SearchViewModel<CommonBlit> searchViewModel,Pageable pageable)
	{
		return searchService.search(searchViewModel, pageable, commonBlitMapper, commonBlitRepository);
	}
	
	public Map<String, Object> searchCommonBlitsForExcel(SearchViewModel<CommonBlit> searchViewModel)
	{
		Set<CommonBlitViewModel> blits = searchService.search(searchViewModel, commonBlitMapper, commonBlitRepository);
		if(blits.stream().findFirst().isPresent()) {
			CommonBlitViewModel blit = blits.stream().findFirst().get();
			if(blit.getAdditionalFields() != null && !blit.getAdditionalFields().isEmpty()) {
				CommonBlit cBlit = commonBlitRepository.findOne(blit.getBlitId());
				Event event = eventRepository.findOne(cBlit.getBlitType().getEventDate().getEvent().getEventId());
				return excelService.getBlitsExcelMap(blits, event.getAdditionalFields());
			}
		}

		return excelService.getBlitsExcelMap(blits);
	}
	
	public Map<String, Object> getExcel(){
		return excelService.getBlitsExcelMap(commonBlitMapper.createFromEntities(new HashSet<>(commonBlitRepository.findAll())));
	}
}
