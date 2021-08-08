//This is for the ui to request data from the api in an abstract sense.
//This one deals with negotiation-related issues.

import 'dart:async';
import 'backendAPI.dart';
import '../../providers/contract.dart';
import '../../providers/condition.dart';

class NegotiationService {

  ApiInteraction api = ApiInteraction();

 Future<dynamic> getNotifications(String party) async {

    return;
 }

 Future<void> saveAgreement (Contract agr) async { //Save initial version of agreement to backend
    //TODO list:
   //Add exception handling. Maybe change return type to String

   Map<String, dynamic> response;
   try {
     response = await api.postData('/negotiation/create-agreement', agr.toJson());

     if (response['Status'] != 'SUCCESSFUL')
       throw Exception('Agreement could not be saved');
   } on Exception catch(e) {
      //Handle exception
     print (e);
     throw e;
   }

 }

 Future<void> saveCondition(Condition cond) async { //Save a condition associated with a contract

    //TODO list:
   //Review return type and error handling (for special cases)

   Map<String, dynamic> response;
   try {
     response = await api.postData('/negotiation/create-condition', cond.toJson());

     if (response['Status'] != 'SUCCESSFUL')
       throw Exception('Condition could not be saved');
   } on Exception catch(e) {
     //Handle exception
     print (e);
     throw e;
   }

 }

  void acceptCondition(String id) async { //Or condition object?

    await _handleCondition(id, true);
  }

  void rejectCondition(String id) async {

    await _handleCondition(id, false);
  }

  Future<void> _handleCondition(String id, bool acc) async { //Either accept or reject condition

   Map<String, String> body = {'ConditionID' : id};
   String path = acc ? 'accept-condition' : 'reject-condition';

   Map<String, dynamic> response;
   try {
     response = await api.postData('/negotiation/$path', body);

     if (response['Status'] != 'SUCCESSFUL')
       throw Exception('Condition could not be ' + (acc ? 'accepted' : 'rejected'));
   } on Exception catch(e) {
     //Handle exception
     print (e);
     return;
   }
 }

 Future<void> setPrice(Contract con) async { //Set the price condition of an agreement.
   //TODO list:
   //Construct body
   //Get endpoint
   //Handle exceptions


 }

  Future<void> setDuration(Contract con) async { //Set the duration condition of an agreement.
    //TODO list:
    //Construct body
    //Get endpoint
    //Handle exceptions


  }

 Future<void> sealAgreement(Contract con) async { //Or pass in Contract?

   Map<String, String> body = {'AgreementID' : con.contractId};

   Map<String, dynamic> response;
   try {
     response = await api.postData('/negotiation/seal-agreement', body);

     if (response['Status'] != 'SUCCESSFUL')
       throw Exception('Agreement could not be sealed');
   } on Exception catch(e) {
     //Handle exception
     print (e);
     return;
   }

 }

}

