import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/models/condition.dart';
import 'package:unison/models/contracts.dart';
import 'package:unison/models/global.dart';
import 'package:unison/services/Server/negotiationService.dart';

class JudgeConditionItem extends StatefulWidget {
  final Condition contractCondition;
  final String party;

  JudgeConditionItem({
    @required this.contractCondition,
    @required this.party,
  });

  @override
  _JudgeConditionItemState createState() => _JudgeConditionItemState();
}

class _JudgeConditionItemState extends State<JudgeConditionItem> {
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

    return widget.party == 'A'
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
                    Text(
                      widget.contractCondition.status + ' by Party B',
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
            //Party B:
            title: Text(
              widget.contractCondition.title == null
                  ? 'Couldn\'t load title'
                  : widget.contractCondition.title,
            ),
            leading: CircleAvatar(
              backgroundColor: Colors.cyan,
            ),
            onTap: () => _showConditionDialog(widget.contractCondition),
            trailing: Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(
                  widget.contractCondition.status + ' by Party A',
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
          );
  }
}
