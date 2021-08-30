//This is for the ui to request data from the api in an abstract sense.
//This one deals with common issues,
//like getting a list of agreements involving a user.
//Exceptions are thrown, and must be handled by the UI. This is subject to change.

import 'dart:async';
import 'package:unison/models/global.dart';
import 'package:unison/services/Server/apiResponse.dart';
import '../../models/contract.dart';
import 'backendAPI.dart';

class CommonService {
  ApiInteraction _api = ApiInteraction();

  Future<List<Contract>> getInvolvedAgreements(String addr) async {
    //Get all agreements where a user is involved
    ApiResponse response = await _api.getData('/user/$addr/agreement');

    if (!response.successful)
      throw Exception('Retrieval of agreements failed');

    List<dynamic> jsonList = ((response.result['agreements']));
    List<Contract> ret = [];
    for (int i = 0; i < jsonList.length; i++) {
      ret.add(Contract.fromJson(jsonList[i]));
    }

    return ret;
  }

  Future<Contract> getAgreement(String id) async {

    ApiResponse response = await _api.getData('/user/${Global.userAddress}/agreement/$id');

    if (!response.successful)
      throw Exception('Retrieval of agreement failed');

    return Contract.fromJson(response.result['AgreementResponse']);

  }


}
