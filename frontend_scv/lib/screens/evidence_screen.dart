import 'package:flutter/material.dart';
import 'package:unison/models/evidenceData.dart';
import 'package:unison/models/global.dart';
import 'package:unison/widgets/agreement/evidence_list_panel.dart';
import 'package:unison/widgets/agreement/upload_evidence_panel.dart';
import 'package:unison/widgets/miscellaneous/funky_text_widget.dart';

class EvidenceScreen extends StatefulWidget {
  @override
  _MessagingScreenState createState() => _MessagingScreenState();
  static const routeName = '/evidence-screen';
}

class _MessagingScreenState extends State<EvidenceScreen> {
  void reload() {
    setState(() {
      super.setState(() {});
    });
  }

  @override
  Widget build(BuildContext context) {
    final args = ModalRoute.of(context).settings.arguments as Map;
    final agreementId = args['agreementId'];

    final partyA = args['partyA'];
    final partyB = args['partyB'];
    bool _isJudge = false;
    if (Global.userAddress != partyA && Global.userAddress != partyB) {
      _isJudge = true;
    }

    return Scaffold(
      appBar: AppBar(
        title: FunkyText('Evidence for Agreement ' + agreementId),
      ),
      body: EvidenceListPanel(
        agreementId,
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.endFloat,
      floatingActionButtonAnimator: FloatingActionButtonAnimator.scaling,
      floatingActionButton:
      _isJudge ? Container() : UploadEvidencePanel(agreementId, reload),
    );
  }
}