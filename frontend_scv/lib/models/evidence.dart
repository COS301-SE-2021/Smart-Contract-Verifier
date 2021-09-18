//This models an item of evidence that a user may send.
//An evidence, if you will.

import 'package:file_picker/file_picker.dart';
import 'package:http/http.dart';

class Evidence {

  //The party that owns the evidence
  String owningParty;
  MultipartFile evidenceFile;
  PlatformFile baseFile;
  String agreementID;

  Evidence(String agId) {
    agreementID = agId;
  }
  //More members may be implemented soon

  Future<void> setFile(PlatformFile f) async {

    baseFile = f;
    evidenceFile = await MultipartFile.fromBytes('uploadEvidence', f.bytes, filename: f.name + DateTime.now().millisecondsSinceEpoch.toString());
  }

}
