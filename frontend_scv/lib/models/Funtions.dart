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