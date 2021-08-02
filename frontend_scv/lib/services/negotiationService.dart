//This is for the ui to request data from the api in an abstract sense.
//This one deals with negotiation-related issues.
//Should these be called services?
//Also, are these multiple files too fragmented?

import 'dart:async';
import '../models/backendAPI.dart';
import '../providers/contract.dart';

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

     if (response['status'] != 'SUCCESSFUL')
       throw Exception('Agreement could not be saved');
   } on Exception catch(e) {
      //Handle exception
     print (e);
     return;
   }

 }

 Future<void> saveCondition(String id, /*Condition*/dynamic cond) async { //Save a condition associated with a contract

    //TODO list:
   //Create Condition class
   //Send to api
   //Review return type (for special cases)
   return;

 }

 Future<void> updateCondition(/*Condition*/ dynamic cond) async {

    //TODO list:
   //Use condition to either remove/resave,
   //or use update functionality if available

    return;
 }


}

