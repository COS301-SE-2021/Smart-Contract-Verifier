import 'package:flutter/material.dart';
import 'package:unison/models/global.dart';
import 'package:unison/services/Server/judgeService.dart';

class JudgeActiveCasesPanel extends StatefulWidget {
  @override
  _JudgeActiveCasesPanelState createState() => _JudgeActiveCasesPanelState();
}

class _JudgeActiveCasesPanelState extends State<JudgeActiveCasesPanel> {
  final _controller = ScrollController();
  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    JudgeService judgeService = JudgeService();

    return FutureBuilder(
        future: judgeService.getInvolvedAgreements(Global.userAddress),
        builder: (context, agreementsSnapshot) {
          return agreementsSnapshot.connectionState != ConnectionState.done
              ? CircularProgressIndicator()
              : agreementsSnapshot.data.length == 0
                  ? Text('No Assignments')
                  : Text(agreementsSnapshot.data.toString());

          // ListView.builder(
          //     controller: _controller,
          //     padding: const EdgeInsets.all(10.0),
          //     itemCount: messagesSnapshot.data.length,
          //     // reverse: true,
          //     // shrinkWrap: true,
          //     itemBuilder: (BuildContext context, int index) {
          //       final message = messagesSnapshot.data[index];
          //       bool isCurrentUser = message.sender == Global.userAddress;
          //       return Container(
          //         margin: EdgeInsets.only(top: 10),
          //         child: Column(
          //           children: [
          //             Row(
          //               mainAxisAlignment: isCurrentUser
          //                   ? MainAxisAlignment.end
          //                   : MainAxisAlignment.start,
          //               crossAxisAlignment: CrossAxisAlignment.end,
          //               children: [
          //                 if (!isCurrentUser)
          //                   CircleAvatar(
          //                     radius: 15,
          //                     // backgroundImage: AssetImage(user.avatar),
          //                   ),
          //                 SizedBox(
          //                   width: 10,
          //                 ),
          //                 Container(
          //                   padding: EdgeInsets.all(10),
          //                   constraints: BoxConstraints(
          //                       maxWidth:
          //                           MediaQuery.of(context).size.width *
          //                               0.6),
          //                   decoration: BoxDecoration(
          //                       color: isCurrentUser
          //                           ? Colors.deepOrange
          //                           : Colors.cyan,
          //                       borderRadius: BorderRadius.only(
          //                         topLeft: Radius.circular(16),
          //                         topRight: Radius.circular(16),
          //                         bottomLeft: Radius.circular(
          //                             isCurrentUser ? 12 : 0),
          //                         bottomRight: Radius.circular(
          //                             isCurrentUser ? 0 : 12),
          //                       )),
          //                   child: Text(
          //                     messagesSnapshot.data[index].messageText,
          //                     // style: MyTheme.bodyTextMessage.copyWith(
          //                     //     color: isCurrentUser ? Colors.white : Colors.grey[800]),
          //                   ),
          //                 ),
          //               ],
          //             ),
          //             Padding(
          //               padding: const EdgeInsets.only(top: 5),
          //               child: Row(
          //                 mainAxisAlignment: isCurrentUser
          //                     ? MainAxisAlignment.end
          //                     : MainAxisAlignment.start,
          //                 children: [
          //                   if (!isCurrentUser)
          //                     SizedBox(
          //                       width: 40,
          //                     ),
          //                   SizedBox(
          //                     width: 8,
          //                   ),
          //                   Text(
          //                     DateFormat('yyyy-MM-dd hh:mm')
          //                         .format(message.dateSent),
          //                     style: TextStyle(
          //                       fontSize: 10,
          //                     ),
          //                   )
          //                 ],
          //               ),
          //             )
          //           ],
          //         ),
          //       );
          //     },
          //   );
        });
  }
}
