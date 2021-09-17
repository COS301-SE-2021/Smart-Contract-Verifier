import 'package:unison/models/evidenceData.dart';

class EvidenceItem {
  EvidenceItem(
      {this.id, this.isExpanded = false, this.metaData, this.agreementId});

  int id;
  bool isExpanded;
  EvidenceData metaData;
  String agreementId;
}
