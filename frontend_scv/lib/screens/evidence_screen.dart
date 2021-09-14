import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:unison/models/evidenceData.dart';
import 'package:unison/widgets/agreement/evidence_list_panel.dart';
import 'package:unison/widgets/agreement/evidence_upload_panel.dart';
import 'package:unison/widgets/miscellaneous/funky_text_widget.dart';
import 'package:mime/mime.dart';

class EvidenceScreen extends StatefulWidget {
  @override
  _MessagingScreenState createState() => _MessagingScreenState();
  static const routeName = '/evidence-screen';
}

class _MessagingScreenState extends State<EvidenceScreen> {
  @override
  Widget build(BuildContext context) {
    final args = ModalRoute.of(context).settings.arguments as Map;
    final agreementId = args['agreementId'];

    final partyA = args['partyA'];
    final partyB = args['partyB'];

    List<EvidenceData> evidences = [];

    return Scaffold(
      appBar: AppBar(
        title: FunkyText('Evidence for Agreement ' + agreementId),
      ),
      body: EvidenceListPanel(agreementId),
    );
  }
}
