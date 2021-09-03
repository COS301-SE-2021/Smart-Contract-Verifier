import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/models/condition.dart';
import 'package:unison/models/contracts.dart';
import 'package:unison/models/global.dart';
import 'package:unison/services/Server/negotiationService.dart';

class ConditionItem extends StatefulWidget {
  final Condition contractCondition;
  final NegotiationService negotiationService;

  ConditionItem({
    @required this.contractCondition,
    @required this.negotiationService,
  });

  @override
  _ConditionItemState createState() => _ConditionItemState();
}

class _ConditionItemState extends State<ConditionItem> {
  var _isLoading = false;

  @override
  void initState() {
    // print('InitState()');
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    _showConditionDialog(Condition contractCondition) {
      showDialog(
          context: context,
          builder: (_) => new AlertDialog(
                title: new Text(contractCondition.conditionId),
                content: new Text(contractCondition.description),
                actions: <Widget>[
                  TextButton(
                    child: Text('Close'),
                    onPressed: () {
                      Navigator.of(context).pop();
                    },
                  )
                ],
              ));
    }

    return Global.userAddress == widget.contractCondition.proposedBy
        ? ListTile(
            //Current user created the condition
            title: Text(
              widget.contractCondition.title == null
                  ? 'Couldn\'t load title'
                  : widget.contractCondition.title,
            ),
            leading: CircleAvatar(
              backgroundColor: Colors.deepOrange,
            ),
            // subtitle: Text('Status: ${contractCondition.status}\nProposed by: '
            //     '${contractCondition.proposedBy}'),
            // subtitle: Text('Status: ${contractCondition.status}\nProposed by: '
            //     '${contractCondition.proposedBy}'),
            onTap: () => _showConditionDialog(widget.contractCondition),
            trailing: Row(
              //The currently logged in user created the condition
              mainAxisSize: MainAxisSize.min,
              children: [
                Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Text('Other Party Response: '),
                    Text(
                      widget.contractCondition.status,
                      style: TextStyle(
                          color: (widget.contractCondition.status == 'ACCEPTED')
                              ? Colors.green
                              : (widget.contractCondition.status == 'REJECTED')
                                  ? Colors.red
                                  : Colors.amber //PENDING

                          ),
                    ),
                  ],
                ),
              ],
            ),
          )
        : ListTile(
            title: Text(
              widget.contractCondition.title == null
                  ? 'Couldn\'t load title'
                  : widget.contractCondition.title,
            ),
            leading: CircleAvatar(
              backgroundColor: Colors.cyan,
            ),
            // subtitle: Text(
            //   'Status: ${contractCondition.status}\nProposed by: '
            //   '${contractCondition.proposedBy}',
            // ),
            onTap: () => _showConditionDialog(widget.contractCondition),
            trailing: Row(
              //The currently logged in user did not create the condition
              mainAxisSize: MainAxisSize.min,
              children: widget.contractCondition.status != 'PENDING'
                  ? widget.contractCondition.status == 'ACCEPTED'
                      ? [
                          //ACCEPTED
                          Text(
                            'ACCEPTED',
                            style: TextStyle(
                              color: Colors.cyan,
                            ),
                          ),
                        ]
                      : [
                          //REJECTED
                          Text(
                            'REJECTED',
                            style: TextStyle(
                              color: Colors.deepOrangeAccent,
                            ),
                          ),
                        ]
                  : [
                      //PENDING
                      // Text('Status: ${contractCondition}'),
                      IconButton(
                        icon: Icon(
                          Icons.thumb_down_outlined,
                          color: Colors.deepOrangeAccent,
                        ),
                        onPressed: () async {
                          print('Accept');
                          setState(() {
                            _isLoading = true;
                          });
                          try {
                            await widget.negotiationService.rejectCondition(
                                widget.contractCondition);
                            print('rejected: ' +
                                widget.contractCondition.conditionId);
                          } catch (error) {
                            await showDialog(
                              context: context,
                              builder: (ctx) => AlertDialog(
                                title: Text('An error occurred!'),
                                content: Text('Something went wrong.'),
                                actions: <Widget>[
                                  TextButton(
                                    child: Text('Okay'),
                                    onPressed: () {
                                      Navigator.of(ctx).pop();
                                    },
                                  )
                                ],
                              ),
                            );
                          }
                          setState(() {
                            _isLoading = false;
                            Provider.of<Contracts>(context, listen: false)
                                .fetchAndSetContracts();
                          });
                        },
                      ),
                      IconButton(
                        icon: Icon(
                          Icons.thumb_up_outlined,
                          color: Colors.cyan,
                        ),
                        onPressed: () async {
                          print('Accept');
                          setState(() {
                            _isLoading = true;
                          });
                          try {
                            await widget.negotiationService.acceptCondition(
                                widget.contractCondition);
                            print('accepted: ' +
                                widget.contractCondition.conditionId);
                          } catch (error) {
                            await showDialog(
                              context: context,
                              builder: (ctx) => AlertDialog(
                                title: Text('An error occurred!'),
                                content: Text('Something went wrong.'),
                                actions: <Widget>[
                                  TextButton(
                                    child: Text('Okay'),
                                    onPressed: () {
                                      Navigator.of(ctx).pop();
                                    },
                                  )
                                ],
                              ),
                            );
                          }
                          setState(() {
                            _isLoading = false;
                            Provider.of<Contracts>(context, listen: false)
                                .fetchAndSetContracts();
                          });
                        },
                      ),
                    ],
            ),
          );
  }
}
