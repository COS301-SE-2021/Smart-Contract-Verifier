//This is for the ui to request data from the api in an abstract sense.
//This one deals with common issues,
//like getting a list of agreements involving a user.

import 'dart:async';

import '../../models/condition.dart';
import '../../models/contract.dart';
import 'backendAPI.dart';

class CommonService {
  ApiInteraction _api = ApiInteraction();

  Contract errorContract = new Contract(
      title: 'Error',
      description:
          'An error was encountered, and the agreement could not be retrieved');
  //Error object to return on exception.

  Future<List<Contract>> getInvolvedAgreements(String addr) async {
    //Get all agreements where a user is involved
    var response;

    try {
      response = await _api.getData('/user/$addr/agreement');

      if (response['Status'] != 'SUCCESSFUL')
        throw Exception('Retrieval of agreements failed');
    } on Exception catch (e) {
      //Handle Exception
      print(e);
      return [];
    }

    List<dynamic> jsonList = ((response['agreements']));
    List<Contract> ret = [];
    for (int i = 0; i < jsonList.length; i++) {
      ret.add(Contract.fromJson(jsonList[i]));
    }

    return ret;
  }




  Future<void> getHello() async {
    //To test api

    print('Call');
    final res = await _api.getData('/negotiation/hello');
    print('Res: ' + res.toString());
  }

  Future<List<Contract>> getAllAgreements() async {
    //Get all agreements in the db. This is purely for testing

    Map<String, String> body = {};
    var response;

    try {
      response =
          await _api.postData('/negotiation/get-agreement', body); //Revise url

      if (response['Status'] != 'SUCCESSFUL')
        throw Exception('Agreements could not be retrieved');
    } on Exception catch (e) {
      //Handle Exception,
      print(e.toString());
      throw e; //This should be revised
    }

    List<dynamic> jsonList = ((response['Agreements']));

    List<Contract> ret = [];
    for (int i = 0; i < jsonList.length; i++) {
      ret.add(Contract.fromJson(jsonList[i]));
    }

    return ret;
  }
}
