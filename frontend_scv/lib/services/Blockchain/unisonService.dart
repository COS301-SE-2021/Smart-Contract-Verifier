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

  ///Save an agreement to the blockchain
  Future<void> saveAgreement(Contract con) async {
    var jsn = con.toJsonChain();

    String data = con.dataToChain();
    String partyB =
        (Global.userAddress == jsn['PartyB']) ? jsn['PartyA'] : jsn['PartyB'];
    EthereumAddress partyBA = EthereumAddress.fromHex(partyB);
    bool direction = con.payingUser == Global.userAddress;;

    print ('ID: ' + con.contractId);
    try {
      print ('About to seal');
      final res = await _smC.makeWriteCall("createAgreement", [
        partyBA,
        con.duration,
        data,
        con.contractId,
        [EthereumAddress.fromHex(await Global.getContractId('UnisonToken'))],
        [BigInt.from(con.paymentAmount)],
        [direction]
      ]);
      print (res);
    } catch (e) {
      print ('ERROR: ' +e);
    }
  }

  ///Accept an agreement saved on the blockchain
  Future<void> acceptAgreement(Contract con) async {
    final res = await _smC.makeWriteCall('acceptAgreement', [con.blockchainId]);
  }

  ///Get an agreement from the blockchain
  Future<BlockchainAgreement> getAgreement(BigInt id) async {

    final res = await _smC.makeReadCall('getAgreement', [id]);
    return BlockchainAgreement.fromCHAIN(res[0]);
  }

  ///Make the current user a judge
  Future<void> addJuror() async {
    //The smart contract automatically removes the necessary funds from the user.
    await _smC.makeWriteCall('addJuror', []);
  }

  ///Remove the current user from the Unison judge-pool
  Future<void> removeJuror() async {
    await _smC.makeWriteCall('removeJuror', []);
  }

  ///Vote on an agreement
  Future<void> jurorVote(BigInt id, int v) async {
    //Vote yes/no on an agreement
    await _smC.makeWriteCall('jurorVote', [id, BigInt.from(v)]);
  }

  ///Checks if a user is a Juror
  Future<bool> isJuror(EthereumAddress add) async {
    final res = await _smC.makeReadCall('isJuror', [add]);
    return res[0]; //Temporary
  }


  //Does the party believe the agreement was fulfilled.
  //Must happen after contract expiration
  ///Conclusion vote for a party
  Future<void> agreementFulfilled(Contract con, bool vote) async {

    await _voteResolution(con.blockchainId, vote ? 2 : 1);
  }

  Future<void> _voteResolution(BigInt id, int v) async {
    await _smC.makeWriteCall('voteResolution', [id, BigInt.from(v)]);
  }

  ///Get the jury assigned to an agreement
  Future<dynamic> getJury(BigInt id) async {
    final res = await _smC.makeReadCall('getJury', [id]);

    print(res);
    return res[0];
  }

  //A party can pay into the agreement, after it has been moved to the blockchain
  ///Add the payment conditions to an agreement
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
  ///Pay the platform fee for an agreement
  Future<void> payPlatformFee(BigInt id) async {
    await _smC.makeWriteCall('payPlatformFee', [id]);
  }

  ///Pay the amount specified in the agreement payment condition
  Future<void> payAgreementMoney(BigInt id) async {
    await _smC.makeWriteCall('payIn', [id]);
  }

}
