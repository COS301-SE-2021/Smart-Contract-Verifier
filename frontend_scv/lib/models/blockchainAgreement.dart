//This class models an agreement already put on the blockchain.
//Not all of the features of the agreement are present here.

import 'package:unison/models/blockchainPayment.dart';
import 'package:unison/models/global.dart';

enum AgreementState {
  PENDING, PROPOSED, REJECTED, ACCEPTED, ACTIVE, COMPLETED, SETTLED, CONTESTED, DECIDED, CLOSED
}

//A party's vote on the success of the agreement
enum PartyVote {
  NONE, NO, YES
}
//Explanation of some states:
//PENDING: The agreement basically does not exist
//PROPOSED: Someone has created an agreement on the blockchain, awaiting acceptance
//REJECTED: Was not accepted.....
//ACCEPTED: Other party accepted, awaiting platform fee payment
//ACTIVE: Platform fee has been paid, and the agreement is official
//COMPLETED: The agreement has concluded



class BlockchainAgreement {

  String serverID;
  String partyA;
  String partyB;
  BigInt resTime; //ResolutionTime
  int state;
  AgreementState stateEnum;
  PartyVote voteEnum; //What the current user voted
  BlockChainPayment paymentInfo;

  BlockchainAgreement.fromCHAIN(dynamic res) {
    //Generate from smartContract response

    serverID = res[0];
    partyA = res[1].toString().toLowerCase();
    partyB = res[2].toString().toLowerCase();
    resTime = res[3];
    state = res[8].toInt();
    stateEnum = AgreementState.values[state];

    int ind = 9;
    if (Global.userAddress == partyB)
      ind = 10;
    voteEnum = PartyVote.values[res[ind].toInt()];

  }

  //Return whether or not the current user should make an accept request to an existing agreement
  bool shouldAccept() {

    if (state == 1) {
      //Proposed state
      if (partyB == Global.userAddress) {
        //Current user did not put on blockchain

        return true;
      }
    }

    return false;
  }

  AgreementState getAgreementState() {
    return stateEnum;
  }

  PartyVote getResolutionVote() {
    return voteEnum;
  }

  ///Is the current user the one who should pay? This does not account for the payment already being made. Use shouldPay to check if payment has been made
  bool amIPaying() {
    return (paymentInfo.infoSet && paymentInfo.payingUser == Global.userAddress);
  }

  ///Has the required payment been made? This method returns true if it is still outstanding
  bool shouldPay() {
    return (paymentInfo.infoSet && paymentInfo.hasBeenPayed);
  }
}
