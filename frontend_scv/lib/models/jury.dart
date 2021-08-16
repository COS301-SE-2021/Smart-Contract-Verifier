//This class will be used to model the jury for an agreement, including the addresses of the jurors and their votes.

import 'package:unison/models/global.dart';
import 'package:unison/models/juror.dart';

class Jury {

  List<Juror> _jurors = [];

  Jury.fromChain() {
    //Read list from blockchain, create jurors and add to list
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