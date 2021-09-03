//This class will be used to model the jury for an agreement, including the addresses of the jurors and their votes.

import 'package:unison/models/global.dart';
import 'package:unison/models/juror.dart';
import 'package:unison/services/Server/contactService.dart';
import 'package:web3dart/credentials.dart';

class Jury {

  List<Juror> _jurors = [];
  bool assigned;
  BigInt deadline;

  Jury.fromChain(jury) {//Takes the returned jury of the smart contract.
    //Read list from blockchain, create jurors and add to list
    assigned = jury[0];
    deadline = jury[1];

    List<dynamic> addresses = jury[2];
    List<dynamic> votes = jury[3];

    for (var i =0;i<votes.length;i++) {

      _jurors.add(Juror(addresses[i].toString(), votes[i]));
    }

  }

  int getMyVoteNumber() { //Get the raw vote

    for (Juror i in _jurors) {
      if (i.address == Global.userAddress)
        return i.voteNumber;
    }

    return -1; //Invalid
  }

  String getMyVote() { //Get the user's vote

    for (Juror i in _jurors) {
      if (i.address == Global.userAddress)
        return i.vote;
    }

    //Or throw error
    return 'Vote not found';

  }

}