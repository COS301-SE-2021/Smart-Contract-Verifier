import 'package:awesome_loader/awesome_loader.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:unison/models/evidence.dart';
import 'package:unison/models/evidenceData.dart';
import 'package:unison/models/evidenceItem.dart';
import 'package:unison/services/Server/evidenceService.dart';
import 'dart:js' as js;

class EvidenceList extends StatefulWidget {
  final String agreementId;
  final List<EvidenceData> evidenceData;
  EvidenceList(this.evidenceData, this.agreementId);

  @override
  _EvidenceListState createState() => _EvidenceListState();
}

class _EvidenceListState extends State<EvidenceList> {
  List<EvidenceItem> _evidenceItems = [];
  EvidenceService _evidenceService = EvidenceService();
  @override
  void initState() {
    super.initState();
    setState(() {
      for (int i = 0; i < widget.evidenceData.length; i++) {
        _evidenceItems.add(
          EvidenceItem(
            id: i,
            metaData: widget.evidenceData[i],
            agreementId: widget.agreementId,
          ),
        );
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: ExpansionPanelList(
        animationDuration: Duration(milliseconds: 500),
        expandedHeaderPadding: EdgeInsets.all(10),
        dividerColor: Colors.pink,
        expansionCallback: (int index, bool isExpanded) {
          setState(() {
            _evidenceItems[index].isExpanded = !isExpanded;
          });
        },
        children:
            _evidenceItems.map((item) => _buildExpansionPanel(item)).toList(),
      ),
    );
  }

  ExpansionPanel _buildExpansionPanel(EvidenceItem evidenceItem) {
    return ExpansionPanel(
      isExpanded: evidenceItem.isExpanded,
      backgroundColor: Color.fromRGBO(56, 61, 81, 1),
      canTapOnHeader: true,
      headerBuilder: (BuildContext context, bool isExpanded) {
        String fn = '';
        if (evidenceItem.metaData.fileName == null) {
          fn = 'URL / Network Link';
        } else {
          fn = evidenceItem.metaData.fileName.toString();
        }

        return ListTile(
          title: Text('Evidence Name: ' + fn),
          subtitle:
              Text('Uploaded By: ' + evidenceItem.metaData.owner.toString()),
        );
      },
      body: Card(
        color: Color.fromRGBO(56, 61, 81, 0.5),
        child: evidenceItem.metaData.type == EvidenceType.UPLOADED
            ? FutureBuilder(
                future: _evidenceService.getEvidenceU(
                    evidenceItem.metaData, widget.agreementId),
                builder: (context, snap) {
                  if (snap.connectionState != ConnectionState.done)
                    return AwesomeLoader();

                  Evidence ev = snap.data;
                  Image ret = Image.memory(ev.baseFile.bytes);
                  return SizedBox(
                    child: ret,
                    height: 500,
                  );
                },
              )
            : FutureBuilder(
                builder: (context, snap) {
                  Image ret = Image.network(
                    evidenceItem.metaData.url,
                    errorBuilder: (context, error, stackTrace) {
                      return Card(
                        child: Column(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            Text(
                              'Could not load preview. Proceed at your own risk!',
                              style: TextStyle(color: Colors.white),
                            ),
                            RichText(
                              text: new TextSpan(
                                children: [
                                  new TextSpan(
                                    text: evidenceItem.metaData.url,
                                    style: new TextStyle(color: Colors.blue),
                                    recognizer: new TapGestureRecognizer()
                                      ..onTap = () async {
                                        js.context.callMethod('open',
                                            ['${evidenceItem.metaData.url}']);
                                      },
                                  ),
                                ],
                              ),
                            ),
                          ],
                        ),
                      );
                    },
                  );
                  return SizedBox(
                    child: ret,
                    height: 500,
                  );
                },
              ),
      ),
    );
  }
}
