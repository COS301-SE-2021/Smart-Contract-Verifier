//This is for the ui to request data from the api in an abstract sense.
//This one deals with common issues,
//like getting a list of agreements involving a user.

import 'dart:async';
import '../models/backendAPI.dart';
import '../providers/contract.dart';

class CommonService {

  ApiInteraction api = ApiInteraction();

  Future<List<Contract>> getInvolvedAgreements(String party) async {

    Map<String, String> body = {'UserID' : party};
    var response;

    try {
      response = await api.postData('/user/retrieve-user-agreements', body);

      if (response['status'] != 'SUCCESSFUL')
        throw Exception('Retrieval of agreements failed');
    } on Exception catch(e) {
      //Handle Exception
      return [];
    }

    List<dynamic> jsonList = ((response['agreements']));

    List<Contract> ret = [];
    for (int i =0; i<jsonList.length;i++)
    {
      ret.add(Contract.fromJson(jsonList[i]));
    }

    return ret;
  }

  Future<Contract> getAgreement(String id) async { //Get agreement with specified id

    Map<String, String> body = {'AgreementID' : id};
    var response;

    try {
      response = await api.postData('/negotiation/get-agreement-details', body);

      if (response['status'] != 'SUCCESSFUL')
        throw Exception('Agreement could not be retrieved');
    } on Exception catch(e) {
      //Handle Exception,
      //possibly with custom contract object
      print (e.toString());
      return Contract();
    }

    return Contract.fromJson(response['agreementResponse']);
  }

  Future</*Condition*/dynamic> getConditions(String id) async { //Get a list of conditions for an agreement

    return;
  }

  Future<void> getHello() async { //To test api

    print ('Call');
    final res = await api.getData('/negotiation/hello');
    print ('Res: '+res.toString());

  }


}