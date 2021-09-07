//This class handles the evidence that each party can upload in the case of a dispute.
//Evidence must be retrieved and stored on both the backend and possible blockchain.
//A second, contemporary service may be added in blockchain services.

import 'package:unison/models/evidence.dart';
import 'package:unison/services/Server/backendAPI.dart';

class EvidenceService {
  ApiInteraction _api = ApiInteraction();

  ///Store evidence on the server
  Future<void> storeEvidence(Evidence ev) async {
    return;
  }

  ///Get the evidence for an agreement
  Future<List<Evidence>> getEvidence(String id) async {

    return [];
  }
}