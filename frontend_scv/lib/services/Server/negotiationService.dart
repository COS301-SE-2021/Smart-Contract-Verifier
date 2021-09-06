//This is for the ui to request data from the api in an abstract sense.
//This one deals with negotiation-related issues.

import 'dart:async';
import 'package:unison/services/Blockchain/unisonService.dart';
import 'package:unison/services/Server/apiResponse.dart';
import 'package:web3dart/credentials.dart';
import '../../models/condition.dart';
import '../../models/contract.dart';
import '../../models/global.dart';
import 'backendAPI.dart';

class NegotiationService {
  ApiInteraction _api = ApiInteraction();
  final String _reqPath = '/user/';
  UnisonService _uniServ = UnisonService();

  Future<dynamic> getNotifications(String party) async {
    return;
  }

  Future<void> saveAgreement(Contract agr) async {

    ApiResponse response =
          await _api.postData('$_reqPath/${Global.userAddress}/agreement', agr.toJson());
    if (!response.successful)
      throw Exception('Agreement could not be saved');

  }

  Future<void> saveCondition(Condition cond) async {
    //Save a condition associated with a contract
    ApiResponse response = await _api.postData('$_reqPath/${Global.userAddress}/agreement/${cond.agreementId}/condition', cond.toJson());

    if (!response.successful)
      throw Exception('Condition could not be saved');

  }

  void acceptCondition(Condition con) async {
    //Or condition object?

    await _handleCondition(con, true);
  }

  void rejectCondition(Condition con) async {
    await _handleCondition(con, false);
  }

  //This class was made to handle both payment and duration.
  //The api requests bodies for the two are now different.
  Future<void> _handleCondition(Condition con, bool acc) async {
    //Either accept or reject condition

    String path = acc ? 'accept' : 'reject';

    //TODO DANGER WARNING ALERT AND EVERYTHING ELSE, THIS WILL BREAK
    //The ui needs to change the way it calls the function
    ApiResponse response = await _api.putData('/user/${Global.userAddress}/agreement/${con.agreementId}/condition/${con.conditionId}/$path');

    if (!response.successful)
      throw Exception(
            'Condition could not be ' + (acc ? 'accepted' : 'rejected'));

  }

  Future<void> setPayment(String con, String payingUser, double price) async {
    //Set the payment condition of an agreement.

    Map<String, dynamic> body = {
      'Payment' : price,
      'PayingUser' : payingUser,
    };
    ApiResponse response = await _api.postData('/user/${Global.userAddress}/agreement/$con/condition/payment', body);

    if (!response.successful)
       throw Exception('Payment info could not be saved');

  }

  Future<void> setDuration(String con, double dur) async {
    //Set the duration condition of an agreement.

    Map<String, dynamic> body = {
      'Duration': dur,
    };
    ApiResponse response = await _api.postData('/user/${Global.userAddress}/agreement/$con/condition/duration', body);

    if (!response.successful)
       throw Exception('Duration info could not be saved');

  }

  // Future<void> _handlePayDuration(String con, double val, bool price) async {
  //   //Handles both price and duration
  //
  //   Map<String, dynamic> body = {
  //     'ProposedUser': Global.userAddress,
  //     'AgreementID': con,
  //     (price ? 'Payment' : 'Duration'): val
  //   };
  //   Map<String, dynamic> response;
  //   String path = price ? 'payment' : 'duration';
  //
  //   try {
  //     response = await _api.postData(_reqPath + 'set-$path-condition', body);
  //
  //     if (response['Status'] != 'SUCCESSFUL')
  //       throw Exception(
  //           '${(price ? 'Payment' : 'Duration')} could not be saved');
  //   } on Exception catch (e) {
  //     //Handle exception
  //     print(e);
  //     throw e;
  //   }
  // }


  //The api will be updated with a new 'seal flow'
  Future<void> sealAgreement(Contract con) async {
    await _uniServ.saveAgreement(con);
  }
}
