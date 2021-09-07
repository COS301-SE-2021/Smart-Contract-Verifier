import 'package:flutter/material.dart';
import 'package:unison/models/global.dart';
import 'package:unison/services/Server/judgeService.dart';
import '../../models/jury.dart';

import '../../models/contract.dart';

class JudgeDetailInfoPanel extends StatefulWidget {
  final Contract _contract;

  JudgeDetailInfoPanel(this._contract);

  @override
  _JudgeDetailInfoPanelState createState() => _JudgeDetailInfoPanelState();
}

class _JudgeDetailInfoPanelState extends State<JudgeDetailInfoPanel> {
  JudgeService _judgeService = JudgeService();

  var _isLoading = false;
  @override
  Widget build(BuildContext context) {
    return Center(
      child: Card(
        elevation: 15,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            ListTile(
              leading: Icon(Icons.info),
              title: Text(widget._contract.contractId),
              subtitle: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text('Created: ${widget._contract.createdDate}'),
                  FutureBuilder(
                      future:
                          _judgeService.getJury(widget._contract.blockchainId),
                      builder: (context, jurySnapShot) {
                        if (jurySnapShot.connectionState ==
                            ConnectionState.done) {
                          print('Jury fetched');
                          print(jurySnapShot.data.getMyVoteNumber());
                          if (jurySnapShot.data.getMyVoteNumber() == 0) {
                            //JUROR HAS NOT VOTED
                            return Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                ElevatedButton(
                                  onPressed: () async {
                                    setState(() {
                                      _isLoading = true;
                                    });
                                    await _judgeService.vote(
                                        widget._contract.blockchainId, true);
                                    setState(() {
                                      _isLoading = false;
                                    });
                                  },
                                  child: Text('Agreement was fulfilled'),
                                  style: ElevatedButton.styleFrom(
                                    primary: Colors.green,
                                    padding: EdgeInsets.symmetric(
                                        horizontal: 15, vertical: 10),
                                  ),
                                ),
                                SizedBox(
                                  height: 10,
                                ),
                                ElevatedButton(
                                  onPressed: () async {
                                    setState(() {
                                      _isLoading = true;
                                    });
                                    await _judgeService.vote(
                                        widget._contract.blockchainId, false);
                                    setState(() {
                                      _isLoading = false;
                                    });
                                  },
                                  child: Text('Agreement was not fulfilled'),
                                  style: ElevatedButton.styleFrom(
                                    primary: Colors.red,
                                    padding: EdgeInsets.symmetric(
                                        horizontal: 15, vertical: 10),
                                  ),
                                ),
                              ],
                            );
                          }
                          if (jurySnapShot.data.getMyVoteNumber() == 1) {
                            //JUROR HAS VOTED NO
                            return Text('You voted that the agreement was not'
                                ' fulfilled'); //TODO:make nice
                          }
                          if (jurySnapShot.data.getMyVoteNumber() == 2) {
                            //JUROR HAS VOTED YES
                            return Text('You voted that the agreement was'
                                ' fulfilled'); //TODO:make nice
                          }
                          if (jurySnapShot.data.getMyVoteNumber() == -1) {
                            //CURRENT USER NOT IN JURY
                            return Text(
                                'You are not part of this jury'); //TODO:make nice
                          }
                          return Text('Awaiting Elon Approval'); //TODO: We
                          // should not see this
                        }
                        return CircularProgressIndicator();
                      }),
                  SizedBox(
                    width: 15,
                  ),
                ],
              ),
            ),
            Container(
              padding: EdgeInsets.only(
                left: 15,
                right: 15,
                bottom: 10,
              ),
              width: double.infinity,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('Agreement ID: ${widget._contract.contractId}'),
                  Text(
                    'Party A: ${widget._contract.partyA}',
                    style: TextStyle(
                      color: widget._contract.partyA == Global.userAddress
                          ? Colors.deepOrange
                          : Colors.cyan,
                    ),
                  ),
                  Text(
                    'Party B: ${widget._contract.partyB}',
                    style: TextStyle(
                      color: widget._contract.partyB == Global.userAddress
                          ? Colors.deepOrange
                          : Colors.cyan,
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
