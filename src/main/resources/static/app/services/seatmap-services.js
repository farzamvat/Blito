/**
 * Created by soroush on 10/25/17.
 */
angular.module('UiServices')
    .service('seatmapService', function ($http, config) {
        var seatMap = this;
        seatMap.getSvgImage = function (salonName) {
            // return $http.get(config.baseUrl+'/api/blito/v1.0/login');
            return $http.get('../../assets/img/'+salonName+'.svg');

        };
        seatMap.getSeatmapList = function () {
            var queryParam = {
                params : {page: 0, size: 100}
            };
            return $http.get(config.baseUrl+'/api/blito/v1.0/salons', queryParam);

        };
        seatMap.getSalonData = function (uid) {
            return $http.get(config.baseUrl+'/api/blito/v1.0/salons/'+uid);

        };
        seatMap.getSvgSchema = function () {
            // return $http.get(config.baseUrl+'/api/blito/v1.0/login');
            return {
                "uid": "12868962-b435-40a6-a7aa-6031d9bf2249",
                "name": "سالن تست",
                "address": "dmond",
                "longitude": 2.2,
                "latitude": 1.1,
                "numberOfSections": 2,
                "sections": [
                    {
                        "uid": "1",
                        "name": "A",
                        "numberOfRows": 2,
                        "rows": [
                            {
                                "uid": "11",
                                "name": "11",
                                "direction": "LTR",
                                "numberOfSeats": 10,
                                "firstSeatStartingNumber": 1,
                                "lastSeatEndingNumber": 10,
                                "seats": [
                                    {
                                        "uid": "a0327974-d195-43ff-959e-801a8329b598",
                                        "name": "1",
                                        "nextUid": "1c3986a8-84d5-4b02-9d18-2983a6c5e00c",
                                        "prevUid": null,
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "1c3986a8-84d5-4b02-9d18-2983a6c5e00c",
                                        "name": "2",
                                        "nextUid": "c6f3f1e2-bd8a-40d4-9872-c38c846b1150",
                                        "prevUid": "a0327974-d195-43ff-959e-801a8329b598",
                                        "state": "available"
                                    },
                                    {
                                        "uid": "c6f3f1e2-bd8a-40d4-9872-c38c846b1150",
                                        "name": "3",
                                        "nextUid": "8c545538-6a78-4e24-9e24-af39e2b5ec21",
                                        "prevUid": "1c3986a8-84d5-4b02-9d18-2983a6c5e00c",
                                        "state": "not-available"
                                    },
                                    {
                                        "uid": "8c545538-6a78-4e24-9e24-af39e2b5ec21",
                                        "name": "4",
                                        "nextUid": "9cb8a227-a52f-488b-8386-7db3c67eecf0",
                                        "prevUid": "c6f3f1e2-bd8a-40d4-9872-c38c846b1150",
                                        "state": "reserved"
                                    },
                                    {
                                        "uid": "9cb8a227-a52f-488b-8386-7db3c67eecf0",
                                        "name": "5",
                                        "nextUid": "e719f8d5-fc17-4c55-b735-473c04829a4c",
                                        "prevUid": "8c545538-6a78-4e24-9e24-af39e2b5ec21",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "e719f8d5-fc17-4c55-b735-473c04829a4c",
                                        "name": "6",
                                        "nextUid": "7b6e0b02-08e7-4dfa-a0ef-16dbd14624fd",
                                        "prevUid": "9cb8a227-a52f-488b-8386-7db3c67eecf0",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "7b6e0b02-08e7-4dfa-a0ef-16dbd14624fd",
                                        "name": "7",
                                        "nextUid": "ee97a02f-ce97-4eca-a7dd-8d39f5839a58",
                                        "prevUid": "e719f8d5-fc17-4c55-b735-473c04829a4c",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "ee97a02f-ce97-4eca-a7dd-8d39f5839a58",
                                        "name": "8",
                                        "nextUid": "39a7a523-c195-4ce3-a811-21d025209431",
                                        "prevUid": "7b6e0b02-08e7-4dfa-a0ef-16dbd14624fd",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "39a7a523-c195-4ce3-a811-21d025209431",
                                        "name": "9",
                                        "nextUid": "8adce001-1ecf-4ff8-8849-4bf14cf71ca4",
                                        "prevUid": "ee97a02f-ce97-4eca-a7dd-8d39f5839a58",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "8adce001-1ecf-4ff8-8849-4bf14cf71ca4",
                                        "name": "10",
                                        "nextUid": null,
                                        "prevUid": "39a7a523-c195-4ce3-a811-21d025209431",
                                        "state": "sold"
                                    }
                                ]
                            },
                            {
                                "uid": "12",
                                "name": "12",
                                "direction": "LTR",
                                "numberOfSeats": 10,
                                "firstSeatStartingNumber": 1,
                                "lastSeatEndingNumber": 10,
                                "seats": [
                                    {
                                        "uid": "7ae6f071-4ca7-428e-8713-100360aef8da",
                                        "name": "1",
                                        "nextUid": "43a12770-0c6e-41ff-8ab2-ac2cfedcceac",
                                        "prevUid": null,
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "43a12770-0c6e-41ff-8ab2-ac2cfedcceac",
                                        "name": "2",
                                        "nextUid": "dc6717af-4815-49d8-90d4-e0773e0fd176",
                                        "prevUid": "7ae6f071-4ca7-428e-8713-100360aef8da",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "dc6717af-4815-49d8-90d4-e0773e0fd176",
                                        "name": "3",
                                        "nextUid": "74bad237-0b1b-4f07-8848-6f5228fe32a3",
                                        "prevUid": "43a12770-0c6e-41ff-8ab2-ac2cfedcceac",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "74bad237-0b1b-4f07-8848-6f5228fe32a3",
                                        "name": "4",
                                        "nextUid": "79e2a8e9-4eb9-464e-ac77-cb076faa4462",
                                        "prevUid": "dc6717af-4815-49d8-90d4-e0773e0fd176",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "79e2a8e9-4eb9-464e-ac77-cb076faa4462",
                                        "name": "5",
                                        "nextUid": "8ac9a7db-3751-4cad-96d6-c10ee62ab88b",
                                        "prevUid": "74bad237-0b1b-4f07-8848-6f5228fe32a3",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "8ac9a7db-3751-4cad-96d6-c10ee62ab88b",
                                        "name": "6",
                                        "nextUid": "eb75e4e5-5a8a-47b4-b42f-ce7f76aabddc",
                                        "prevUid": "79e2a8e9-4eb9-464e-ac77-cb076faa4462",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "eb75e4e5-5a8a-47b4-b42f-ce7f76aabddc",
                                        "name": "7",
                                        "nextUid": "c70b9582-48d4-4095-819c-27e2d578862b",
                                        "prevUid": "8ac9a7db-3751-4cad-96d6-c10ee62ab88b",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "c70b9582-48d4-4095-819c-27e2d578862b",
                                        "name": "8",
                                        "nextUid": "6352c7a3-89ec-465a-b725-917b353a4842",
                                        "prevUid": "eb75e4e5-5a8a-47b4-b42f-ce7f76aabddc",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "6352c7a3-89ec-465a-b725-917b353a4842",
                                        "name": "9",
                                        "nextUid": "c0dd7cdc-e50b-4e91-a8a4-478d900be763",
                                        "prevUid": "c70b9582-48d4-4095-819c-27e2d578862b",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "c0dd7cdc-e50b-4e91-a8a4-478d900be763",
                                        "name": "10",
                                        "nextUid": null,
                                        "prevUid": "6352c7a3-89ec-465a-b725-917b353a4842",
                                        "state": "sold"
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        "uid": "2",
                        "name": "B",
                        "numberOfRows": 1,
                        "rows": [
                            {
                                "uid": "21",
                                "name": "21",
                                "direction": "LTR",
                                "numberOfSeats": 10,
                                "firstSeatStartingNumber": 1,
                                "lastSeatEndingNumber": 10,
                                "seats": [
                                    {
                                        "uid": "17ae6f071-4ca7-428e-8713-100360aef8da",
                                        "name": "1",
                                        "nextUid": "143a12770-0c6e-41ff-8ab2-ac2cfedcceac",
                                        "prevUid": null,
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "143a12770-0c6e-41ff-8ab2-ac2cfedcceac",
                                        "name": "2",
                                        "nextUid": "1dc6717af-4815-49d8-90d4-e0773e0fd176",
                                        "prevUid": "17ae6f071-4ca7-428e-8713-100360aef8da",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "1dc6717af-4815-49d8-90d4-e0773e0fd176",
                                        "name": "3",
                                        "nextUid": "174bad237-0b1b-4f07-8848-6f5228fe32a3",
                                        "prevUid": "143a12770-0c6e-41ff-8ab2-ac2cfedcceac",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "174bad237-0b1b-4f07-8848-6f5228fe32a3",
                                        "name": "4",
                                        "nextUid": "179e2a8e9-4eb9-464e-ac77-cb076faa4462",
                                        "prevUid": "1dc6717af-4815-49d8-90d4-e0773e0fd176",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "179e2a8e9-4eb9-464e-ac77-cb076faa4462",
                                        "name": "5",
                                        "nextUid": "18ac9a7db-3751-4cad-96d6-c10ee62ab88b",
                                        "prevUid": "174bad237-0b1b-4f07-8848-6f5228fe32a3",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "18ac9a7db-3751-4cad-96d6-c10ee62ab88b",
                                        "name": "6",
                                        "nextUid": "1eb75e4e5-5a8a-47b4-b42f-ce7f76aabddc",
                                        "prevUid": "179e2a8e9-4eb9-464e-ac77-cb076faa4462",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "1eb75e4e5-5a8a-47b4-b42f-ce7f76aabddc",
                                        "name": "7",
                                        "nextUid": "1c70b9582-48d4-4095-819c-27e2d578862b",
                                        "prevUid": "18ac9a7db-3751-4cad-96d6-c10ee62ab88b",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "1c70b9582-48d4-4095-819c-27e2d578862b",
                                        "name": "8",
                                        "nextUid": "16352c7a3-89ec-465a-b725-917b353a4842",
                                        "prevUid": "1eb75e4e5-5a8a-47b4-b42f-ce7f76aabddc",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "16352c7a3-89ec-465a-b725-917b353a4842",
                                        "name": "9",
                                        "nextUid": "1c0dd7cdc-e50b-4e91-a8a4-478d900be763",
                                        "prevUid": "1c70b9582-48d4-4095-819c-27e2d578862b",
                                        "state": "sold"
                                    },
                                    {
                                        "uid": "1c0dd7cdc-e50b-4e91-a8a4-478d900be763",
                                        "name": "10",
                                        "nextUid": null,
                                        "prevUid": "16352c7a3-89ec-465a-b725-917b353a4842",
                                        "state": "sold"
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        };
    });
