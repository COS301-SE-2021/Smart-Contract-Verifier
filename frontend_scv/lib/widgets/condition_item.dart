import 'package:flutter/material.dart';
import 'package:unison/providers/condition.dart';
import 'package:unison/providers/global.dart';
import 'package:unison/services/Server/negotiationService.dart';

class ConditionItem extends StatelessWidget {
  final Condition contractCondition;
  final NegotiationService negotiationService;

  ConditionItem({
    @required this.contractCondition,
    @required this.negotiationService,
  });

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

    return Global.userAddress == contractCondition.proposedBy
        ? ListTile(
            //Current user created the condition
            title: Text(
              contractCondition.title == null
                  ? 'Couldn\'t load title'
                  : contractCondition.title,
            ),
            leading: CircleAvatar(
              backgroundColor: Colors.deepOrange,
            ),
            // subtitle: Text('Status: ${contractCondition.status}\nProposed by: '
            //     '${contractCondition.proposedBy}'),
            subtitle: Text('Status: ${contractCondition.status}\nProposed by: '
                '${contractCondition.proposedBy}'),
            onTap: () => _showConditionDialog(contractCondition),
            trailing: Row(
              //The currently logged in user created the condition
              mainAxisSize: MainAxisSize.min,
              children: [
                Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Text('Status: '),
                    Text(
                      contractCondition.status,
                      style: TextStyle(
                          color: (contractCondition.status == 'ACCEPTED')
                              ? Colors.green
                              : (contractCondition.status == 'REJECTED')
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
              contractCondition.title == null
                  ? 'Couldn\'t load title'
                  : contractCondition.title,
            ),
            leading: CircleAvatar(
              backgroundColor: Colors.cyan,
              // backgroundImage: NetworkImage(contract.imageUrl),
            ),
            subtitle: Text('Status: ${contractCondition.status}\nProposed by: '
                '${contractCondition.proposedBy}'),
            onTap: () => _showConditionDialog(contractCondition),
            trailing: Row(
              //The currently logged in user did not create the condition
              mainAxisSize: MainAxisSize.min,
              children: [
                // Text('Status: ${contractCondition}'),
                IconButton(
                  icon: Icon(
                    Icons.thumb_down_outlined,
                    color: Colors.deepOrangeAccent,
                  ),
                  onPressed: () async {
                    print('Reject');
                  },
                ),
                IconButton(
                  icon: Icon(
                    Icons.thumb_up_outlined,
                    color: Colors.cyan,
                  ),
                  onPressed: () async {
                    print('Accept');
                    await negotiationService
                        .acceptCondition(contractCondition.conditionId);
                    print('accepted: ' + contractCondition.conditionId);
                  },
                ),
              ],
            ),
          );
  }
}
