import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/providers/condition.dart';

import '../providers/auth.dart';
import '../providers/contract.dart';

class ConditionItem extends StatelessWidget {
  final Condition contractCondition;
  ConditionItem({
    @required this.contractCondition,
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
      subtitle: Text('Status: Todo'),
      onTap: () => _showConditionDialog(contractCondition),
      trailing: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          IconButton(
            icon: Icon(
              Icons.thumb_down_outlined,
              color: Colors.deepOrangeAccent,
            ),
            onPressed: () {
              print('Reject');
            },
          ),
          IconButton(
            icon: Icon(
              Icons.thumb_up_outlined,
              color: Colors.cyan,
            ),
            onPressed: () {
              print('Accept');
            },
          ),
        ],
      ),
    );
  }
}
