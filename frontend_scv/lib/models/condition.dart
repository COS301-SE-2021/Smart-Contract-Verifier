import 'package:flutter/material.dart';

//Condition class, the terms of an agreement
class Condition with ChangeNotifier {
  //Other attributes may be added as necessary
  String agreementId;
  String conditionId;
  String title;
  String proposedBy;
  String description;
  String status; //Pending, accepted etc.

  Condition(
      {this.agreementId,
      this.title,
      this.proposedBy,
      this.description,
      this.status = 'Pending',
      this.conditionId = ''}); //Constructor

  Condition.fromJson(Map<String, dynamic> jsn)
      : agreementId = jsn['AgreementID'],
        conditionId = jsn['ConditionID'],
        title = jsn['ConditionTitle'],
        proposedBy = jsn['ProposingUser']['publicWalletID'], //TODO: publicWalletID will be uppercase soon
        description = jsn['ConditionDescription'],
        status = jsn['ConditionStatus'];

  Map<String, dynamic> toJson() => { //For saving a condition to the server
        //Convert instance to JSON
        'AgreementID': agreementId,
        'ConditionTitle': title,
        'ProposedUser': proposedBy,
        'ConditionDescription': description,
        'ConditionStatus' :status,
      };
}
