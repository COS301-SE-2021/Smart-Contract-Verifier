//This is an item to be displayed in a list.
//Should be clickable to display the actual evidence file


import 'package:flutter_linkify/flutter_linkify.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:awesome_loader/awesome_loader.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:unison/models/evidence.dart';
import 'package:unison/models/evidenceData.dart';
import 'package:unison/services/Server/evidenceService.dart';

class EvidenceDisplayItem extends StatefulWidget {
  final EvidenceData metaData;
  final String agreementID;

  EvidenceDisplayItem(this.metaData, this.agreementID);

  @override
  _EvidenceDisplayItemState createState() => _EvidenceDisplayItemState();
}

class _EvidenceDisplayItemState extends State<EvidenceDisplayItem> {
  bool displayItem = false;
  EvidenceService evs = EvidenceService();

  @override
  Widget build(BuildContext context) {
    return displayItem
        ? Center( //File is displayed
            child: Column(
              children: [
                ListTile(
                  title: Text('Evidence ID: ' + widget.metaData.id),
                  tileColor: Color.fromRGBO(102, 10, 102, 1.0),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10),
                  ),
                ),
                Container(
                  margin: EdgeInsets.symmetric(vertical: 15, horizontal: 10),
                    child: widget.metaData.type == EvidenceType.UPLOADED? FutureBuilder(
                        future: evs.getEvidenceU(
                            widget.metaData, widget.agreementID),
                        builder: (context, snap) {

                          if (snap.connectionState != ConnectionState.done)
                            return AwesomeLoader();

                          Evidence ev = snap.data;
                          Image ret = Image.memory(ev.baseFile.bytes);
                          return SizedBox(child: ret, height: 500,);
                          //return ret;
                        }) :
                    FutureBuilder(
                        builder: (context, snap) {
                          Image ret = Image.network(widget.metaData.url,
                            errorBuilder: (context, error, stackTrace) {
                              return SizedBox(height: 5, child: ListTile(shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(10),
                              ), title: Text(
                                  'Could not load preview. Proceed at your own risk!', style: TextStyle(color: Colors.white),),
                                subtitle: Linkify(
                                  onOpen: (link) async {
                                    //print("Linkify link = ${link.url}");
                                    if (await canLaunch (link.url)) {

                                      await launch(link.url);
                                    }
                                  },
                                  text: "Url: " + widget.metaData.url,
                                  style: TextStyle(color: Colors.white),
                                  linkStyle: TextStyle(color: Colors.red),
                                ),
                                tileColor: Color.fromRGBO(
                                    2, 8, 24, 0.7294117647058823),
                              ),
                               );
                            },);
                          //ret.ori
                          return SizedBox(child: ret, height: 500,);
                          //return ret;
                        })
                )
              ],
            ),
          )
        : GestureDetector( //File is not displayed
            onTap: () {
              print("Tapped....");
              displayItem = true;
              setState(() {
                build(context);
              });
            },
            child: Center(
              child: ListTile(
                title: Text('Evidence ID: ' + widget.metaData.id),
                tileColor: Color.fromRGBO(102, 10, 102, 1.0),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(10),
                ),
              ),
            ),
          );
  }
}