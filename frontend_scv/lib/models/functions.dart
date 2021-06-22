import 'dart:convert';
import 'dart:html';
import 'dart:js';
import 'package:http/http.dart';
import 'package:flutter/material.dart';

String address = "address1";
String address2 = "address2";
final String deployedURL = "http://localhost:8080/";

class Agreement { //Most of these are String for now

  String agreementID;
  int duration;
  String partyA;
  String partyB;
  String createdDate;
  String sealedDate;
  List<dynamic> conditions;
  String status;
  String movedToBlockChain;

  Agreement.fromJson(Map<String, dynamic> json)
      : agreementID = json['agreementID'],
        duration = json['duration'],
        partyA = json['PartyA'],
        partyB = json['PartyB'],
        createdDate = json['createdDate'],
        sealedDate = json['sealedDate'],
        status = json['status'],
        movedToBlockChain = json['movedToBlockChain'],
        conditions = json['conditions'];

//Can add ToJSON constructor if ever necessary
}


//API requests here. CTRL F to find this line
Future<String> createInitialAgreement(String addressO, String addressT) async { //Create agreement on the backend.
  final result = await post(
    Uri.parse(deployedURL + '/negotiation/create-agreement'), //And create agreement path
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(<String, String>{
      'PartyA' : addressO,
      'PartyB' : addressT
    }),
  );

  Map<String, dynamic> agrMap = jsonDecode(result.body);
  String status = agrMap['status'];

  if (status != 'SUCCESSFUL')
    throw Exception("Agreement could not be created");

  String agrID = agrMap['agreementID']; //AgreementID is not of type string if status != success, so must be gotten after check
  return agrID;
}

Future<Agreement> getAgreement(String contractID) async { //Get agreement from the backend. #Note: Use Flutter class, generate instance from JSON
  final result = await post(
    Uri.parse(deployedURL + 'negotiation/get-agreement-details'),
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(<String, String>{
      'AgreementID' : contractID
    }),
  );

  Map<String, dynamic> agrMap = jsonDecode(result.body);
  return Agreement.fromJson(agrMap);

}

void sealAgreement(String contractID) async { //Make an agreement final on backend.
  final result = await post(
    Uri.parse(deployedURL + 'negotiation/get-agreement-details'),
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(<String, String>{
      'AgreementID' : contractID
    }),
  );

  Map<String, dynamic> agrMap = jsonDecode(result.body);
  String status = agrMap['status']; //Handle from here

}

Future<String> proposeCondition(String contract, String condition) async { //Create condition/term for a contract.

  final result = await post(
    Uri.parse(deployedURL + 'negotiation/create-condition'), //And create condition path
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(<String, String>{
      'ProposedUser' : 'Thing1',
      'AgreementID' : contract,
      'ConditionDescription' : condition
    }),
  );

  print (result.body);
  Map<String, dynamic> agrMap = jsonDecode(result.body);

  String status;
  try { //If status is not a string, then the kotlin server ran into an error and generates an integer status
    status = agrMap['status'];
  }  on Exception catch(_)
  {
    int st = agrMap['status'];
    throw Exception("Condition not created.\n The server responded with a numerical error code: " + st.toString());
  }

  print (result.body);
  if (status != 'SUCCESSFUL')
    throw Exception("Condition could not be created");

  String condID = agrMap['conditionID']; //Successful request, get condID
  return condID;

}

Future<String> acceptCondition(String condition) async { //Accept proposed condition. Each condition has a unique ID
  final result = await post(
    Uri.parse(deployedURL +'negotiation/accept-condition'),
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(<String, String>{
      'ConditionID' : condition
    }),
  );

  Map<String, dynamic> agrMap = jsonDecode(result.body);
  String status = agrMap['status'];

  // if (status != "SUCCESSFUL") //May be unnecessary check
  //   throw Exception('Payment could not be set');

  return status;
}

void rejectCondition(String condition) async {
  final result = await post(
    Uri.parse(deployedURL + 'negotiation/reject-condition'),
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(<String, String>{
      'ConditionID' : condition
    }),
  );

  Map<String, dynamic> agrMap = jsonDecode(result.body);
  String status = agrMap['status'];

  if (status != "SUCCESSFUL")
    throw Exception('Condition could not be rejected');

}

Future<List<dynamic>> getConditions(String agreement) async { //Get the conditions associated with an agreement
  final result = await post(
    Uri.parse(deployedURL + 'negotiation/get-all-conditions'), //And accept condition path
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(<String, String>{
      'AgreementID' : agreement
    }),
  );

  Map<String, dynamic> agrMap = jsonDecode(result.body);
  String status = agrMap['status'];
  if (status != "SUCCESSFUL")
    throw Exception('Conditions could not be retrieved');

  List<dynamic> conds = agrMap['conditions'];
  return conds;
}

void setPayment(String agreement, double amount) async { //Set payment condition of agreement
  final result = await post(
    Uri.parse(deployedURL + 'negotiation/set-payment-condition'),
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(<String, String>{
      'ProposedUser' : address,
      'AgreementID' : agreement,
      'Payment' : amount.toString()
    }),
  );

  Map<String, dynamic> agrMap = jsonDecode(result.body);
  String status = agrMap['status'];

  if (status != "SUCCESSFUL")
    throw Exception('Payment could not be set');

  String condID = agrMap['conditionID'];
}

void setDuration(String agreement, int time) async { //Set duration  condition of agreement
  final result = await post(
    Uri.parse(deployedURL + 'negotiation/set-payment-condition'),
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(<String, String>{
      'ProposedUser' : address,
      'AgreementID' : agreement,
      'Duration' : time.toString()
    }),
  );

  Map<String, dynamic> agrMap = jsonDecode(result.body);
  String status = agrMap['status'];
  if (status != "SUCCESSFUL")
    throw Exception('Duration could not be set');

  String condID = agrMap['conditionID'];
}

void getCondDetails(String condID) async { //Set duration  condition of agreement
  final result = await post(
    Uri.parse(deployedURL + 'negotiation/set-payment-condition'),
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(<String, String>{
      'ConditionID' : condID
    }),
  );

  Map<String, dynamic> agrMap = jsonDecode(result.body);
  String status = agrMap['status'];

  if (status != "SUCCESSFUL")
    throw Exception('Details could not be retrieved');

}

void showNotify(BuildContext context, String line) //Function to show alertDialogue to avoid some code duplication. To be used soon.
{
  showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          content: Text(line),
        );
      });
}


//End of backend API requests