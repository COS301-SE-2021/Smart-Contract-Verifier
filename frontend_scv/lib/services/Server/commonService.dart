//This is for the ui to request data from the api in an abstract sense.
//This one deals with common issues,
//like getting a list of agreements involving a user.

import 'dart:async';
import 'backendAPI.dart';
import '../../providers/contract.dart';
import '../../providers/condition.dart';

class CommonService {

  ApiInteraction api = ApiInteraction();
  Contract errorContract = new Contract(title: 'Error', description: 'An error was encountered, and the agreement could not be retrieved');
  //Error object to return on exception.

  Future<List<Contract>> getInvolvedAgreements(String party) async {

    Map<String, String> body = {'UserID' : party};
    var response;

    try {
      response = await api.postData('/user/retrieve-user-agreements', body);

      if (response['Status'] != 'SUCCESSFUL')
        throw Exception('Retrieval of agreements failed');
    } on Exception catch(e) {
      //Handle Exception
      print(e);
      return [Contract(title: 'Error', description: e.toString())]; //This should be revised
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

      if (response['Status'] != 'SUCCESSFUL')
        throw Exception('Agreement could not be retrieved');
    } on Exception catch(e) {
      print (e.toString());
      throw e;
    }

    return Contract.fromJson(response['AgreementResponse']);
  }

  Future<List<Condition>> getConditions(String id) async { //Get a list of conditions for an agreement

    List<Condition> res = [];

    Map<String, dynamic> response;
    Map<String, dynamic> body = {'AgreementID' : id};

    try {
      response = await api.postData('', body);

      if (response['Status'] != 'SUCCESSFUL')
        throw Exception('Conditions could not be retrieved');
    } on Exception catch(e) {
      print (e.toString());
      throw e;
    }

    for (int i =0;i<response['Conditions'].length;i++) {
      res.add(Condition.fromJson(response['Conditions'][i]));
    }

    return res;
  }

  Future<void> getHello() async { //To test api

    print ('Call');
    final res = await api.getData('/negotiation/hello');
    print ('Res: '+res.toString());

  }
  Future<List<Contract>> getAllAgreements() async { //Get all agreements in the db. This is purely for testing

    Map<String, String> body = {};
    var response;

    try {
      response = await api.postData('/negotiation/get-agreement', body); //Revise url

      if (response['Status'] != 'SUCCESSFUL')
        throw Exception('Agreements could not be retrieved');
    } on Exception catch(e) {
      //Handle Exception,
      print (e.toString());
      throw e; //This should be revised
    }

    List<dynamic> jsonList = ((response['Agreements']));

    List<Contract> ret = [];
    for (int i =0; i<jsonList.length;i++)
    {
      ret.add(Contract.fromJson(jsonList[i]));
    }

    return ret;
  }


}