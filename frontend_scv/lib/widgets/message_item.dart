import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/providers/condition.dart';
import 'package:unison/providers/contracts.dart';
import 'package:unison/providers/global.dart';
import 'package:unison/providers/message.dart';
import 'package:unison/services/Server/messageService.dart';
import 'package:unison/services/Server/negotiationService.dart';

class MessageItem extends StatefulWidget {
  final Message message;
  final MessageService messageService;

  MessageItem({
    @required this.message,
    @required this.messageService,
  });

  @override
  _ConditionItemState createState() => _ConditionItemState();
}

class _ConditionItemState extends State<MessageItem> {
  var _isLoading = false;

  @override
  void initState() {
    // print('InitState()');
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    _showMessageDialog(Message _message) {
      showDialog(
          context: context,
          builder: (_) => new AlertDialog(
                title: new Text('Message info if wanted'),
                // content: new Text(contractCondition.description),
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

    return Global.userAddress == widget.message.sender
        ? ListTile(
            //Current user sent the message
            title: Text(
              widget.message.messageText == null
                  ? 'Couldn\'t load message.'
                  : widget.message.messageText,
            ),
            leading: CircleAvatar(
              backgroundColor: Colors.deepOrange,
            ),
            onTap: () => _showMessageDialog(widget.message),
            trailing: Row(
              //The currently logged in user created the condition
              mainAxisSize: MainAxisSize.min,
              children: [
                Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Text('--timestamp--'),
                    // Text(
                    //   widget.contractCondition.status,
                    //   style: TextStyle(
                    //       color: (widget.contractCondition.status == 'ACCEPTED')
                    //           ? Colors.green
                    //           : (widget.contractCondition.status == 'REJECTED')
                    //               ? Colors.red
                    //               : Colors.amber //PENDING
                    //
                    //       ),
                    // ),
                  ],
                ),
              ],
            ),
          )
        : ListTile(
            title: Text(
              widget.message.messageText == null
                  ? 'Couldn\'t load title'
                  : widget.message.messageText,
            ),
            leading: CircleAvatar(
              backgroundColor: Colors.cyan,
            ),
            subtitle: Text('Sender: ${widget.message.sender}'),
            onTap: () => _showMessageDialog(widget.message),
          );
  }
}
