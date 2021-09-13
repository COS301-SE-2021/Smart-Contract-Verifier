import 'package:flutter/material.dart';
import 'package:unison/models/condition.dart';

class JudgeConditionItem extends StatefulWidget {
  final Condition contractCondition;
  final String party;
  final String durationId;
  final String paymentId;

  JudgeConditionItem({
    @required this.contractCondition,
    @required this.party,
    this.durationId,
    this.paymentId,
  });

  @override
  _JudgeConditionItemState createState() => _JudgeConditionItemState();
}

class _JudgeConditionItemState extends State<JudgeConditionItem> {
  @override
  void initState() {
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
            leading: Icon(
              widget.contractCondition.conditionId == widget.paymentId
                  ? Icons.paid
                  : widget.contractCondition.conditionId == widget.durationId
                      ? Icons.today
                      : Icons.face,
              color: Colors.pinkAccent,
            ),
            subtitle: Text('${widget.contractCondition.description}'),
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
            leading: Icon(
              widget.contractCondition.conditionId == widget.paymentId
                  ? Icons.paid
                  : widget.contractCondition.conditionId == widget.durationId
                      ? Icons.today
                      : Icons.perm_identity,
              color: Colors.cyan,
            ),
            subtitle: Text('${widget.contractCondition.description}'),
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
