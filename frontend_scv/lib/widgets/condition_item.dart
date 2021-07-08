import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../providers/auth.dart';
import '../providers/contract.dart';

class ConditionItem extends StatelessWidget {
  final dynamic contractCondition;
  ConditionItem({
    @required this.contractCondition,
  });

  @override
  Widget build(BuildContext context) {
    _showConditionDialog(dynamic contractCondition) {
      showDialog(
          context: context,
          builder: (_) => new AlertDialog(
                //TODO: read from dynamic
                title: new Text(contractCondition.toString()),
                content: new Text("This is where  the Condition description "
                    "will go, it needs to be read from a dynamic object - TODO"),
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
      //TODO: read from dynamic
      title: Text(contractCondition.toString()),
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
