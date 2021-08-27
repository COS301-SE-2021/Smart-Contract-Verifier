//This class models an agreement already put on the blockchain.
//Not all of the features of the agreement are present here.

import 'package:unison/models/global.dart';

enum AgreementState {
  PENDING, PROPOSED, REJECTED, ACCEPTED, ACTIVE, COMPLETED, SETTLED, CONTESTED, DECIDED, CLOSED
}

class BlockchainAgreement {
  String serverID;
  String partyA;
  String partyB;
  BigInt resTime; //ResolutionTime
  int state;
  AgreementState stateEnum;

  BlockchainAgreement.fromCHAIN(dynamic res) {
    //Generate from smartContract response

    serverID = res[0];
    partyA = res[1].toString().toLowerCase();
    partyB = res[2].toString().toLowerCase();
    resTime = res[3];
    state = res[8].toInt();
    stateEnum = AgreementState.values[state];

  }

  //Return whether or not the current user should make an accept request to an existing agreement
  bool shouldAccept() {
    print('PARTY B: ' + partyB);
    print('GLOBAL: ' + Global.userAddress);
    print('STATE: ' + state.toString());
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


// switch (state) {
//
//   case 0 : ret = 'Pending';
//           break;
//   case 1: ret = 'Proposed';
//           break;
//   case 2: ret = 'Rejected';
//           break;
//   case 3 : ret = 'Accepted';
//           break;
//   case 4: ret = 'Active'; //Has been accepted
//           break;
//   case 5: ret = 'Completed';
//           break;
//   case 6: ret = 'Settled';
//           break;
//   case 7: ret = 'Contested';
//           break;
//   case 8: ret = 'Decided';
//           break;
//   case 9: ret = 'Closed';
//           break;
//
// }

}
