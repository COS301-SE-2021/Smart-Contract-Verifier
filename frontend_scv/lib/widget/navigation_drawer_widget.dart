import 'package:flutter/material.dart';

// import '../../page/action_required_page.dart';
// import '../../page/user_profile_page.dart';
import '../../page/demo_page.dart';

class NavigationDrawerWidget extends StatelessWidget {
  final padding = EdgeInsets.symmetric(horizontal: 20);

  @override
  Widget build(BuildContext context) {
    final name = 'Nicolas Cage';
    final walletId = 'rr33-vffd-3345';
    final urlImage =
        'https://img1.looper.com/img/gallery/the-biggest-nicolas-cage-movies-of-all-time/intro-1614010892.jpg';

    return Drawer(
      child: Material(
        color: Color.fromRGBO(37, 37, 37, 1),
        child: ListView(
          children: <Widget>[
            buildHeader(
                urlImage: urlImage,
                name: name,
                walletId: walletId,
                onClicked: () => null
                // onClicked: () => Navigator.of(context).push(
                //   MaterialPageRoute(
                //     builder: (context) => UserProfilePage(
                //       name: name,
                //       urlImage: urlImage,
                //     ),
                //   ),
                // ),
                ),
            Container(
              padding: padding,
              child: Column(
                children: [
                  const SizedBox(height: 12),
                  // // buildSearchField(),
                  // const SizedBox(height: 24),
                  // buildMenuItem(
                  //   text: 'Activity',
                  //   icon: Icons.feed_outlined,
                  //   onClicked: () => selectedItem(context, 1),
                  // ),
                  // const SizedBox(height: 16),
                  // buildMenuItem(
                  //   text: 'Required Action',
                  //   icon: Icons.favorite_border,
                  //   onClicked: () => selectedItem(context, 2),
                  // ),
                  // const SizedBox(height: 16),
                  // buildMenuItem(
                  //   text: 'Judge Duty',
                  //   icon: Icons.gavel_outlined,
                  //   onClicked: () => selectedItem(context, 3),
                  // ),
                  // // const SizedBox(height: 24),
                  // const SizedBox(height: 48),
                  // const SizedBox(height: 48),
                  // Divider(color: Colors.white70),
                  // const SizedBox(height: 16),
                  // buildMenuItem(
                  //   text: 'Support',
                  //   icon: Icons.support,
                  //   onClicked: () => selectedItem(context, 4),
                  // ),
                  // const SizedBox(height: 16),
                  // buildMenuItem(
                  //   text: 'Settings',
                  //   icon: Icons.settings,
                  //   onClicked: () => selectedItem(context, 5),
                  // ),
                  const SizedBox(height: 16),
                  buildMenuItem(
                    text: 'Demo 1',
                    icon: Icons.settings,
                    onClicked: () => selectedItem(context, 6),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget buildHeader({
    required String urlImage,
    required String name,
    required String walletId,
    required VoidCallback onClicked,
  }) =>
      InkWell(
        onTap: onClicked,
        child: Container(
          padding: padding.add(EdgeInsets.symmetric(vertical: 40)),
          child: Row(
            children: [
              CircleAvatar(radius: 30, backgroundImage: NetworkImage(urlImage)),
              SizedBox(width: 20),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    name,
                    style: TextStyle(fontSize: 20, color: Colors.white),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    walletId,
                    style: TextStyle(fontSize: 14, color: Colors.white),
                  ),
                ],
              ),
              Spacer(),
              CircleAvatar(
                radius: 20,
                backgroundColor: Colors.tealAccent,
                child: Icon(Icons.fit_screen_outlined,
                    color: Color.fromRGBO(37, 37, 37, 1)),
              )
            ],
          ),
        ),
      );

  Widget buildSearchField() {
    final color = Colors.white;

    return TextField(
      style: TextStyle(color: color),
      decoration: InputDecoration(
        contentPadding: EdgeInsets.symmetric(horizontal: 20, vertical: 15),
        hintText: 'Search',
        hintStyle: TextStyle(color: color),
        prefixIcon: Icon(Icons.search, color: color),
        filled: true,
        fillColor: Colors.white12,
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(5),
          borderSide: BorderSide(color: color.withOpacity(0.7)),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(5),
          borderSide: BorderSide(color: color.withOpacity(0.7)),
        ),
      ),
    );
  }

  Widget buildMenuItem({
    required String text,
    required IconData icon,
    VoidCallback? onClicked,
  }) {
    final color = Colors.white;
    final hoverColor = Colors.white70;

    return ListTile(
      leading: Icon(icon, color: color),
      title: Text(text, style: TextStyle(color: color)),
      hoverColor: hoverColor,
      onTap: onClicked,
    );
  }

  void selectedItem(BuildContext context, int index) {
    Navigator.of(context).pop();

    switch (index) {
      // case 0:
      //   Navigator.of(context).push(MaterialPageRoute(
      //     builder: (context) => UserProfilePage(
      //       name: "user name",
      //       urlImage: "img url",
      //     ),
      //   ));
      //   break;
      // case 1:
      //   Navigator.of(context).push(MaterialPageRoute(
      //     builder: (context) => ActivityPage(),
      //   ));
      //   break;
      // case 2:
      //   Navigator.of(context).push(MaterialPageRoute(
      //     builder: (context) => ActionRequiredPage(),
      //   ));
      //   break;
      // case 3:
      //   Navigator.of(context).push(MaterialPageRoute(
      //     builder: (context) => JudgeDutyPage(),
      //   ));
      //   break;
      // case 4:
      //   Navigator.of(context).push(MaterialPageRoute(
      //     builder: (context) => SupportPage(),
      //   ));
      //   break;
      // case 5:
      //   Navigator.of(context).push(MaterialPageRoute(
      //     builder: (context) => SettingsPage(),
      //   ));
      //   break;
      case 6:
        Navigator.of(context).push(MaterialPageRoute(
          builder: (context) => DemoPage(),
        ));
        break;
    }
  }
}
