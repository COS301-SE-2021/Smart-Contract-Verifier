import 'package:flutter/material.dart';

//Condition class, the terms of an agreement
class Condition with ChangeNotifier {
  //Other attributes may be added as necessary
  String agreementId;
  String conditionId;
  String title;
  String proposedBy;
  String description;

  Condition(
      {this.agreementId,
      this.title,
      this.proposedBy,
      this.description,
      this.conditionId = ''}); //Constructor

  Condition.fromJson(Map<String, dynamic> jsn)
      : agreementId = jsn['AgreementID'],
        conditionId = jsn['ConditionID'],
        title = jsn['ConditionTitle'],
        proposedBy = jsn['ProposingUser']['publicWalletID'],
        // proposedBy = jsn['ProposedUser']['PublicWalletID'],
        description = jsn['ConditionDescription'];

  Map<String, dynamic> toJson() => {
        //Convert instance to JSON
        'AgreementID': agreementId,
        'ConditionTitle': title,
        'ProposedUser': proposedBy,
        'ConditionDescription': description,
      };
}
