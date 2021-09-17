//This widget displays a list of all evidence of an agreement.
//Clicking on evidence should display the actual file.

import 'package:awesome_loader/awesome_loader.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:unison/models/evidence.dart';
import 'package:unison/models/evidenceData.dart';
import 'package:unison/models/evidenceItem.dart';
import 'package:unison/services/Server/evidenceService.dart';
import 'package:unison/widgets/agreement/evidence_list.dart';

import 'evidence_display_item.dart';

class EvidenceListPanel extends StatefulWidget {
  final String agreementId;
  EvidenceListPanel(this.agreementId);

  @override
  _EvidenceListPanelState createState() => _EvidenceListPanelState();
}

class _EvidenceListPanelState extends State<EvidenceListPanel> {
  EvidenceService evidenceService = EvidenceService();
  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: evidenceService.getEvidenceData(widget.agreementId),
      builder: (context, snapshot) {
        List<Widget> cards = [];
        if (snapshot.connectionState != ConnectionState.done) {
          return AwesomeLoader();
        }
        return EvidenceList(snapshot.data, widget.agreementId);
      },
    );
  }
}
