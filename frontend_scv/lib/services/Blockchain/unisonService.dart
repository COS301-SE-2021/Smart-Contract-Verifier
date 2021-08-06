//This class will be used to interact with the Unison smart Contract to save and get contracts from the blockchain

import '../../providers/contract.dart';
import 'smartContract.dart';

class UnisonService {

  SmartContract _smC = SmartContract();

  Future<void> saveAgreement(Contract con) async {
    //Todo list:
    //Use relevant function name to call smart contract
    //Use con to construct parameters
    //In a real deployed contract, what will the delay be?
    //When will a result be returned?
    //How should it be handled in the UI?

  }


}