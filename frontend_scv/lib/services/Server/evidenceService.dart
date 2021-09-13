//This class handles the evidence that each party can upload in the case of a dispute.
//Evidence must be retrieved and stored on both the backend and possible blockchain.
//A second, contemporary service may be added in blockchain services.

import 'package:file_picker/file_picker.dart';
import 'package:unison/models/evidence.dart';
import 'package:unison/models/evidenceData.dart';
import 'package:unison/models/global.dart';
import 'package:unison/services/Server/apiResponse.dart';
import 'package:unison/services/Server/backendAPI.dart';

class EvidenceService {
  ApiInteraction _api = ApiInteraction();

  ///Store evidence for an agreement on the server
  Future<void> storeEvidence(Evidence ev) async {

    ApiResponse res = await _api.filePost('/user/${Global.userAddress}/agreement/${ev.agreementID}/evidence/upload', ev.evidenceFile);
  }

  Future<void> linkEvidence(String url, String id) async {
    var body = {'EvidenceUrl' : url};
    ApiResponse res = await _api.postData('/user/${Global.userAddress}/agreement/$id/evidence/link', body);
  }

  ///Get the evidence IDs and hashes for an agreement
  Future<List<EvidenceData>> getEvidenceData(String id) async {

    print ('One');
    ApiResponse res = await _api.getData('/user/${Global.userAddress}/agreement/$id/evidence/');
    print ('Two');
    List<dynamic> evs = res.result['UploadedEvidenceDetails'];
    evs.addAll(res.result['LinkedEvidenceDetails']);
    print ('Three');
    List<EvidenceData> ret = [];
    print ('About to start');
    for (var i in evs) {
      //ret.add(EvidenceData.fromString(i));
      ret.add(EvidenceData.fromJSON(i));
    }

    return ret;
  }

  ///Get the Evidence file for a given evidenceData (Uploaded type)
  Future<Evidence> getEvidenceU(EvidenceData evd, String id) async {
    PlatformFile res = await _api.fileGet('/user/${Global.userAddress}/agreement/$id/evidence/${evd.id}/download');
    //print ('File :' + res.name);
    Evidence ev = Evidence(id);
    ev.baseFile = res;
    return ev;
  }

  ///Get the Evidence file for a given evidenceData (Linked type)
  Future<Evidence> getEvidenceL(EvidenceData evd, String id) async {
    return Evidence(id);
  }
}