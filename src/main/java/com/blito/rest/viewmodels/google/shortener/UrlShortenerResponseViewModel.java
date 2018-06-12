package com.blito.rest.viewmodels.google.shortener;

public class UrlShortenerResponseViewModel {
	private Integer status_code;
	private String status_text;
	private Data data;

	@Override
	public String toString() {
		return "UrlShortenerResponseViewModel{" +
				"status_code=" + status_code +
				", status_text='" + status_text + '\'' +
				", data=" + data.toString() +
				'}';
	}

	public Integer getStatus_code() {
		return status_code;
	}

	public void setStatus_code(Integer status_code) {
		this.status_code = status_code;
	}

	public String getStatus_text() {
		return status_text;
	}

	public void setStatus_text(String status_text) {
		this.status_text = status_text;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data {
		private String url;
		private String hash;
		private String global_hash;
		private String long_url;
		private Integer new_hash;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getHash() {
			return hash;
		}

		public void setHash(String hash) {
			this.hash = hash;
		}

		public String getGlobal_hash() {
			return global_hash;
		}

		public void setGlobal_hash(String global_hash) {
			this.global_hash = global_hash;
		}

		public String getLong_url() {
			return long_url;
		}

		public void setLong_url(String long_url) {
			this.long_url = long_url;
		}

		public Integer getNew_hash() {
			return new_hash;
		}

		public void setNew_hash(Integer new_hash) {
			this.new_hash = new_hash;
		}

		@Override
		public String toString() {
			return "Data{" +
					"url='" + url + '\'' +
					", hash='" + hash + '\'' +
					", global_hash='" + global_hash + '\'' +
					", long_url='" + long_url + '\'' +
					", new_hash=" + new_hash +
					'}';
		}
	}

}

