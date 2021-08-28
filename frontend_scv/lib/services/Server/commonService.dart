//This is for the ui to request data from the api in an abstract sense.
//This one deals with common issues,
//like getting a list of agreements involving a user.

import 'dart:async';

import '../../models/condition.dart';
import '../../models/contract.dart';
import 'backendAPI.dart';

class CommonService {
  ApiInteraction _api = ApiInteraction();

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


}
