import 'package:flutter/material.dart';

//Condition class, the terms of an agreement
class Condition with ChangeNotifier {

  String agreementId;
  String title;
  String proposedBy;
  String description;

  Condition({this.agreementId, this.title, this.proposedBy, this.description}); //Constructor

  Condition.fromJson(Map<String, dynamic> jsn) //Create instance from JSON
      : agreementId = jsn['AgreementID'],
        title = jsn['ConditionTitle'],
        proposedBy = jsn['ProposedUser'],
        description = jsn['ConditionDescription'];

  Map<String, dynamic> toJson() => { //Convert instance to JSON
    'AgreementID': agreementId,
    'ConditionTitle': title,
    'ProposedUser': proposedBy,
    'ConditionDescription': description,
  };

}