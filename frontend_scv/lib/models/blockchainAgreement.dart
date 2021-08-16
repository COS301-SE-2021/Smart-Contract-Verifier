//This class models an agreement already put on the blockchain.
//Not all of the features of the agreement are present here.

import 'package:unison/models/global.dart';

class BlockchainAgreement {

  String serverID;
  String partyA;
  String partyB;
  BigInt resTime; //ResolutionTime
  int state;


  BlockchainAgreement.fromCHAIN(dynamic res) { //Generate from smartContract response

    serverID = res[0];
    partyA = res[1];
    partyB = res[2];
    resTime = res[3];
    state = res[9];

  }

  //Return whether or not the current user should make an accept request to an existing agreement
  bool shouldAccept() {

    if (state == 3) { //Proposed state
        if (partyB == Global.userAddress) { //Current user did not put on blockchain

          return true;
        }
    }

    return false;
  }

}