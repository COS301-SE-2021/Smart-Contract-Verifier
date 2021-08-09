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

    return ListTile(
      title: Text(
        contractCondition.title == null
            ? 'Couldn\'t load title'
            : contractCondition.title,
      ),
      leading: CircleAvatar(
        backgroundColor: Colors.black12,
        // backgroundImage: NetworkImage(contract.imageUrl),
      ),
      subtitle: Text('Status: ${contractCondition.status}\n Proposed by: '
          '${contractCondition.proposedBy}'),
      // subtitle: Text(contractCondition.proposedBy),

      onTap: () => _showConditionDialog(contractCondition),
      trailing: Global.userAddress == contractCondition.proposedBy
          ? Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text('Proposed by you - Status: TODO'),
                // IconButton(
                //   icon: Icon(
                //     Icons.thumb_down_outlined,
                //     color: Colors.deepOrangeAccent,
                //   ),
                //   onPressed: () {
                //     print('Reject');
                //   },
                // ),
                // IconButton(
                //   icon: Icon(
                //     Icons.thumb_up_outlined,
                //     color: Colors.cyan,
                //   ),
                //   onPressed: () {
                //     print('Accept');
                //   },
                // ),
              ],
            )
          : Row(
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
