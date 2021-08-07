//This is for the ui to request data from the api in an abstract sense.
//This one deals with negotiation-related issues.
//Should these be called services?
//Also, are these multiple files too fragmented?

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

   Map<String, dynamic> agrMap = agr.toJson();
   Map<String, String> body = {'PartyA' : agrMap['PartyA'], 'PartyB' : agrMap['PartyB'], 'AgreementTitle' : agrMap['title'],
     'AgreementDescription' : agrMap['description'], 'AgreementImageURL' : agrMap['imageUrl']};

   Map<String, dynamic> response;
   try {
     response = await api.postData('/negotiation/create-agreement', body);

     if (response['Status'] != 'SUCCESSFUL')
       throw Exception('Agreement could not be saved');
   } on Exception catch(e) {
      //Handle exception
     print (e);
     return;
   }

 }

 Future<void> saveCondition(String id, Condition cond) async { //Save a condition associated with a contract

    //TODO list:
   //Review return type and error handling (for special cases)

   Map<String, dynamic> con = cond.toJson();
   Map<String, String> body = {'AgreementID' : id, 'ConditionTitle' : con['ConditionTitle'],
     'ProposedUser' : con['ProposedUser'], 'ConditionDescription' : con['ConditionDescription']}; //Maybe change proposedUser to map to the current user? To ensure
   // it's correct, since only the proposing user can save a condition.

   Map<String, dynamic> response;
   try {
     response = await api.postData('/negotiation/create-condition', body);

     if (response['Status'] != 'SUCCESSFUL')
       throw Exception('Condition could not be saved');
   } on Exception catch(e) {
     //Handle exception
     print (e);
     return;
   }

 }

  void acceptCondition(String id) async { //Or condition object?

    await handleCondition(id, true);
  }

  void rejectCondition(String id) async {

    await handleCondition(id, false);
  }

  Future<void> handleCondition(String id, bool acc) async { //Either accept or reject condition

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

 Future<void> updateCondition(Condition cond) async { //May not be necessary

    //TODO list:
   //Use condition to either remove/resave,
   //or use update functionality if available

    return;
 }


 Future<void> sealAgreement(String id) async { //Or pass in Contract?

   Map<String, String> body = {'AgreementID' : id};

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

