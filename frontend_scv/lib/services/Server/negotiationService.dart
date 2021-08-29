//This is for the ui to request data from the api in an abstract sense.
//This one deals with negotiation-related issues.

import 'dart:async';
import 'package:unison/services/Blockchain/unisonService.dart';
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
    //Save initial version of agreement to backend
    //TODO list:
    //Add exception handling. Maybe change return type to String

    Map<String, dynamic> response;
    try {
      response =
          await _api.postData('$_reqPath/${Global.userAddress}/agreement', agr.toJson());
      print(response.toString());
      if (response['Status'] != 'SUCCESSFUL')
        throw Exception('Agreement could not be saved');
    } on Exception catch (e) {
      //Handle exception
      print(e);
      throw e;
    }
  }

  Future<void> saveCondition(Condition cond) async {
    //Save a condition associated with a contract

    Map<String, dynamic> response;
    try {
      response =
          await _api.postData('$_reqPath/${Global.userAddress}/agreement/${cond.agreementId}/condition', cond.toJson());

      if (response['Status'] != 'SUCCESSFUL')
        throw Exception('Condition could not be saved');
    } on Exception catch (e) {
      //Handle exception
      print(e);
      throw e;
    }
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

    //TODO DANGER WARNING ALERT EVERYTHING ELSE THIS WILL BREAK,
    //The ui needs to change the way it calls the function
    Map<String, dynamic> response;
    try {
      /*response = */await _api.putData('/user/${Global.userAddress}/agreement/${con.agreementId}/condition/${con.conditionId}/$path');

      if (response['Status'] != 'SUCCESSFUL')
        throw Exception(
            'Condition could not be ' + (acc ? 'accepted' : 'rejected'));
    } on Exception catch (e) {
      //Handle exception
      print(e);
      return;
    }
  }

  Future<void> setPayment(String con, String payingUser, double price) async {
    //Set the payment condition of an agreement.

    Map<String, dynamic> body = {
      'Payment' : price,
      'PayingUser' : payingUser,
    };
    var response;

    try {
      response = await _api.postData('/user/${Global.userAddress}/agreement/$con/condition/payment', body);

      if (response['Status'] != 'SUCCESSFUL')
        throw Exception('Payment info could not be saved');
    } on Exception catch (e) {
      //Handle exception
      print(e);
      throw e;
    }

  }

  Future<void> setDuration(String con, double dur) async {
    //Set the duration condition of an agreement.

    Map<String, dynamic> body = {
      'Duration': dur,
    };
    var response;

    try {
      response = await _api.postData('/user/${Global.userAddress}/agreement/$con/condition/duration', body);

      if (response['Status'] != 'SUCCESSFUL')
        throw Exception('Duration info could not be saved');
    } on Exception catch (e) {
      //Handle exception
      print(e);
      throw e;
    }
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

  //This method is bound to change soon.
  //The api will be updated with a new 'seal flow'
  Future<void> sealAgreement(Contract con) async {
    Map<String, dynamic> response;
    try {
      response = await _api.postData(
          _reqPath + 'seal-agreement', {'AgreementID': con.contractId});

      if (response['Status'] != 'SUCCESSFUL')
        throw Exception('Agreement could not be sealed');

      //Save the agreement on the blockchain
      await _uniServ.saveAgreement(con);
      //await _uniServ.addPaymentConditions(con.blockchainId, EthereumAddress.fromHex(con.payingUser), BigInt.from(con.price.toInt()));
    } on Exception catch (e) {
      //Handle exception
      print(e);
      throw (e);
    }
  }
}
