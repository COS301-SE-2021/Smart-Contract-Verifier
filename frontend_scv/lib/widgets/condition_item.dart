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
                title: new Text(contractCondition.toString()), //TODO
                content: new Text("Condition description (TODO)"),
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

    return GestureDetector(
      onTap: () => _showConditionDialog(contractCondition),
      child: ListTile(
        title: Text(contractCondition.toString()),
        leading: CircleAvatar(
          backgroundColor: Colors.black12,
          // backgroundImage: NetworkImage(contract.imageUrl),
        ),
        subtitle: Text('Status: Todo'),
        trailing: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            IconButton(
              icon: Icon(
                Icons.thumb_down_outlined,
                color: Colors.deepOrangeAccent,
              ),
              onPressed: null,
            ),
            IconButton(
              icon: Icon(
                Icons.thumb_up_outlined,
                color: Colors.cyan,
              ),
              onPressed: null,
            ),
          ],
        ),
      ),
    );
  }
}
