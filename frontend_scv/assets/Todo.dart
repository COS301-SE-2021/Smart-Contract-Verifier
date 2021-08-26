// Dashboard screen
//// Notifications (for Agreement activity): Surprised Pikachu
/*
 * -> List of Notification objects
*/

// New Agreement Screen : Touch ups
//// Save a new/empty Agreement object
/*
 * -> New Agreement object save to server
 *    * How do we ensure the other party is on the server already? (ASSUME
 * for now)
*/

// Agreement Details Screen
//// Retrieve Agreement with conditions array in Agreement object
/*
 * -> Get Agreement object from server DONE
 * -> Get Agreement condition objects from server DONE
 * -> Insert Agreement conditions into a list and make it part of the
 * agreement object DONE
 -> Messaging & Evidence (half)
 *
*/
// Judge Duty Screen
//// Involved Agreements (where user is a jury member): (GET LEGIT)
/*
 * -> List of Agreement objects
*/
//// Notifications (for Jury activity):
/*
 * -> List of Notification objects

*
* NB: LOWER CASE ADDRESSES
* https://pub.dev/packages/identicon
*
* @Ronan: Maybe we can have a metamaskDisconnect (disconnect metamask from
* the app) for logout functionality
*
* Generic API Handler (Hairdu example)
*
* 0xf808ee4efb1b80ebd803f02b8f99fcc4a5a65709 (Main Account)
* 0x351277a110948cb5be266c53ea85737e28ac35bf (Secondary Account)
*
* Wednesday Night:
* - Special Conditions:
*   -x Payment Condition (both can set) (there can be multiple) -> DONE
*   -x Duration Condition () -> DONE
*
* - Seal Agreement /
*   Migrate to blockchain, once mined enough, on blockchain == true
*
*   If no conditions are {PENDING}
*   - enable the seal agreement button
*   If both parties 'Seal' -> move to blockchain
*
*   If one party requests seal and another
**
*
* - Check if user exists
*    -> PartyB in create agreement screen
*    -> First Time Login (check user is in db)
* - Show no Agreements message if no agreements

* ---------------
* TODO After Demo:
* ---------------
* Jury chat   --> Change Colors
* Payment condition must be more flexible (state which party pays)
* Add payment condition to blockchain (Based on backend payment condition)
* Pay Platform once accepted on blockchain
* Once vote has been cast - Remove Buttons (say what they voted) DONE/ make
* pretty
* Clear Input on submit --> messages
*
* ---------------
* TODO Before Demo:
* ---------------
* Sign up to be jury (button with description) DONE
* Duration input -> make hours and convert to seconds DONE
* ---------------
* LATER:
* ---------------
* Evidence
* Notifications
* ---------------
*
* Running Everything:
* 1) Update Server files (CORS)
* 2) deploy SC
*     - get address
*     - set in application.properties
* 3)
*
* (After Kaleb's Individual contribution)
* Kevin (Frontend Development):
*   - Learn Flutter framework (which uses Dart)
*   - Design Screens & Widgets
*   - Manage State within the Application
*   - Input validation
*   - Themes, coloring, and styling
*   - Building Mock data to use in the Frontend before it was integrated with
*  the backend
*   - Working with Ronan to integrate with the server and smart-contract
*   - Next up for individual contribution is Ronan.
*
* */
