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
        'ConditionTitle': title,
        'ConditionDescription': description,
      };

  ///Generate a string of the condition in the format it will be stored in on the blockchain.
  String toChain() {
    String ret = '[' + title + '#' + description + ']';
    return ret;
  }

  ///Generate a condition from blockchain data
  Condition.fromChainData(String data) {
      title = data.substring(0, data.indexOf('#'));
      description = data.substring(data.indexOf('#') +1);
  }

  ///Ensures that two conditions are equal
  bool valid(Condition con) {
    return title == con.title && description == con.description;
  }

  String toString() {
    return '(Condition) Title: ' + title + '\n' + '(Condition) Description: ' + description;
  }
}
