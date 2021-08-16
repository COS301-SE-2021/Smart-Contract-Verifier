//This class will be used to interact with the Unison smart Contract to save and get contracts from the blockchain
//The jury is also handled by Verifier, so those functions are here as well.

import 'package:web3dart/credentials.dart';
import '../../models/blockchainAgreement.dart';
import '../../models/contract.dart';
import '../../models/global.dart';
import 'smartContract.dart';

class UnisonService {
  //For the Verifier smart contract

  SmartContract _smC =
      SmartContract("JSON/_src_Verifier_sol_Verifier.abi", 'Verifier');

  Future<void> saveAgreement(Contract con) async {
    //Todo list:
    //Sort out events, and detecting them.

    var jsn = con.toJsonChain();

    String data = con.title + '#' + con.description;
    String partyB =
        (Global.userAddress == jsn['PartyB']) ? jsn['PartyA'] : jsn['PartyB'];
    EthereumAddress partyBA = EthereumAddress.fromHex(partyB);
    print('A: ' + Global.userAddress);

    final res = await _smC.makeWriteCall("createAgreement", [
      partyBA,
      con.duration,
      data,
      con.contractId,
    ]); //Soon, this will be replaced by a spinner in the UI
    // It will have to check for an event.

    final ev = await _smC.getCreationSubscription();
    await ev.asFuture(); //Wait for the block to be added
    await ev.cancel();

    print(res);
  }

  Future<void> acceptAgreement(Contract con) async {
    //This should probably be called immediately after the contract is sealed on backend.
    print(con.movedToBlockchain);
    print('ACCEPTR AGREEMENT:' + con.blockchainId.toString());
    // if (!con.movedToBlockchain) {
    //   throw Exception('Agreement is not on blockchain yet');
    // }TODO

     final res = await _smC.makeWriteCall('acceptAgreement', [con.blockchainId]);
  }

  Future<BlockchainAgreement> getAgreement(BigInt id) async {

    if (id == null)
//      return null;
        print('Was null');

    final res = await _smC.makeReadCall('getAgreement', [id]);
    return BlockchainAgreement.fromCHAIN(res[0]);
  }

  Future<void> addJuror() async {
    //The smart contract automatically removes the necessary funds from the user.
    await _smC.makeWriteCall('addJuror', []);
  }

  Future<void> removeJuror() async {
    await _smC.makeWriteCall('removeJuror', []);
  }

  Future<void> jurorVote(BigInt id, int v) async {
    //Vote yes/no on an agreement
    await _smC.makeWriteCall('jurorVote', [id, BigInt.from(v)]);
  }

  Future<bool> isJuror(EthereumAddress add) async {
    final res = await _smC.makeReadCall('isJuror', [add]);
    print('Actual res: ' + res.toString());
    return res[0]; //Temporary
  }

  //Does the party believe the agreement was fulfilled.
  //Must happen after contract expiration
  Future<void> agreementFulfilled(Contract con, bool vote) async {
    //If contract has not reached resolution date yet.
    // if (con.sealedDate.millisecondsSinceEpoch + con.duration.toInt()*1000< DateTime.now().millisecondsSinceEpoch) {
    //   throw Exception('Contract has not reached resolution date');
    // }

    await _voteResolution(con.blockchainId, vote ? 2 : 1);
  }

  Future<void> _voteResolution(BigInt id, int v) async {
    await _smC.makeWriteCall('voteResolution', [BigInt.from(0), BigInt.from(v)]);
  }

  Future<dynamic> getJury(BigInt id) async {
    final res =  await _smC.makeReadCall('getJury', [id]);

    print(res);
    return res[0];
  }

  //For testing
  Future<void> setEvent() async {
    final res = await _smC.getCreationSubscription();
    print('Starting await');
    await res.asFuture();
    print('Starting cancel');
    await res.cancel();
    print('Done');
  }

  //A party can pay into the agreement, after it has been moved to the blockchain
  Future<void> addPaymentConditions(
      BigInt id, EthereumAddress address, BigInt amount) async {
    final res = await _smC.makeWriteCall('addPaymentConditions', [
      id,
      [address],
      [amount]
    ]);
  }

  //Pay the platform fee for an agreement
  //Someone (anyone) has to pay the platform fee after the agreement is accepted, and that will make it active.
  Future<void> payPlatformFee(BigInt id) async {
    print('Paying fee for ' + id.toString());
    await _smC.makeWriteCall('payPlatformFee', [id]);
  }
}
