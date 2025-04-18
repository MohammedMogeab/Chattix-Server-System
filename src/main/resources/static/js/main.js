'use strict';

const chatPage = document.querySelector('#chat-page');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');

let stompClient = null;
let nickname = "ali";
let fullname = "ali";
let selectedUserId = null;
let selectUsername=null;
let unreadCount=0;
let unreadGroupCount= 0;
let groupNames = [];
let selectGroupName=null;
let selectedGroupId=null;

window.onload = function () {
//    nickname = document.querySelector('#nickname').value.trim();
//    fullname = document.querySelector('#fullname').value.trim();
//    console.log(`Nickname: ${nickname}, Fullname: ${fullname}`);  // Add this line to check values

    // Retrieve user details from localStorage
//    const nickname = localStorage.getItem('username');
//    const fullname = localStorage.getItem('username');

   if (nickname && fullname) {
//        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);


        stompClient.connect({}, onConnected, onError);
    }

    event.preventDefault();
};










document.getElementById("showUsersBtn").addEventListener("click", function () {
    document.getElementById("usersListSection").classList.remove("hidden");
    document.getElementById("groupsListSection").classList.add("hidden");
    document.getElementById("othersListSection").classList.add("hidden");
});

document.getElementById("showGroupsBtn").addEventListener("click", function () {
    document.getElementById("usersListSection").classList.add("hidden");
    document.getElementById("groupsListSection").classList.remove("hidden");
    document.getElementById("othersListSection").classList.add("hidden");
});

document.getElementById("showOthersBtn").addEventListener("click", function () {
    document.getElementById("usersListSection").classList.add("hidden");
    document.getElementById("groupsListSection").classList.add("hidden");
    document.getElementById("othersListSection").classList.remove("hidden");
});



function onConnected() {
    stompClient.subscribe(`/user/${nickname}/queue/messages`, onMessageReceived);
    stompClient.subscribe(`/user/public`, onMessageReceived);

    // Fetch groups and wait for them to be fetched before proceeding
  fetchGroupsByUser(nickname).then(groupNames => {
      console.log("Fetched groups:", JSON.stringify(groupNames, null, 2)); // This will print the whole array with proper indentation

      if (groupNames.length > 0) {
          groupNames.forEach(group => {
              // Print the entire group object
              console.log("Group details:", JSON.stringify(group, null, 2)); // Pretty print each group object

              // Now you can subscribe to the group, assuming 'group' has a name or ID
              // For example, if the group has a 'name' property
              subscribeToGroup(group.groupId); // Adjust this based on the structure of the group
          });
      } else {
          console.log('No groups found to subscribe to.');
      }
  }).catch(error => {
      console.log("Error fetching groups:", error);
  });

    // Register the connected user
    stompClient.send("/app/user.addUser",
        {},
        JSON.stringify({nickName: nickname, fullName: fullname, status: 'ONLINE'})
    );
    document.querySelector('#connected-user-fullname').textContent = fullname;
    findAndDisplayConnectedUsers().then();
    findAndDisplayUserGroups().then();
}


async function findAndDisplayConnectedUsers() {
    try {
    console.log("the name is "+nickname);
        const response = await fetch(`http://localhost:8087/api/chat/list/${nickname}`); // Replace '1' with the actual logged-in user ID
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        let chatUsers = await response.json();
        const connectedUsersList = document.getElementById('connectedUsers');
        connectedUsersList.innerHTML = ''; // Clear existing list
            console.log(chatUsers);
        chatUsers.forEach(chatEntry => {
            appendUserElement(chatEntry, connectedUsersList);
        });



       } catch (error) {
        console.error("Error fetching chat users 😂😂:", error);
    }
}

async function fetchGroupsByUser(nickname) {
    try {
        const response = await fetch(`http://localhost:8087/api/groups/users/${nickname}/groups`);
         console.log("this is inside fetchGroupsByUser");
        if (!response.ok) {
            throw new Error('Failed to fetch groups');
        }

        const groups = await response.json();
         groupNames = groups.map(group => group.groupId); // Store group names globally
            console.log("Groups fetched: ", groupNames);
        return groups;
    } catch (error) {
        console.error('Error fetching groups:', error);
        return []; // Return empty array if there's an error
    }
}



// group list
async function findAndDisplayUserGroups() {
    try {
        console.log("Fetching groups for: " + nickname);
      const userGroups =await fetchGroupsByUser(nickname);

        if (userGroups.length === 0) {
            console.log("No groups found.");
            return;
        }

        const connectedGroupsList = document.getElementById('connectedGroups');
        connectedGroupsList.innerHTML = ''; // Clear old list

        console.log(userGroups);
        userGroups.forEach(group => {
            appendGroupElement(group, connectedGroupsList);
        });

    } catch (error) {
        console.error("Error fetching user groups 😅:", error);
    }
}

// end group list

// group append
function appendGroupElement(group, container) {
    const listItem = document.createElement('li');
    listItem.classList.add('group-item');
    listItem.setAttribute('id', `group-${group.groupId}`);
    listItem.setAttribute('info', group.groupName);

    // Group image
    const groupImage = document.createElement('img');
    groupImage.src = '../img/group_icon.png'; // Ensure this image exists
    groupImage.alt = group.groupName;

    // Group info wrapper
    const groupInfo = document.createElement('div');
    groupInfo.classList.add('group-info');

    // Group name
    const groupNameSpan = document.createElement('span');
    groupNameSpan.textContent = group.groupName;

    // Created by
    const createdBySpan = document.createElement('span');
    createdBySpan.textContent = `By: ${group.createdBy.username}`;
    createdBySpan.classList.add('creator');

    // Created at
    const createdAtSpan = document.createElement('span');
    createdAtSpan.textContent = new Date(group.createdAt).toLocaleDateString();
    createdAtSpan.classList.add('created-at');

    // Assemble group info
    groupInfo.appendChild(groupNameSpan);
    groupInfo.appendChild(createdBySpan);
    groupInfo.appendChild(createdAtSpan);

    // Assemble list item
    listItem.appendChild(groupImage);
    listItem.appendChild(groupInfo);

    // Optional: click event
    listItem.addEventListener('click', groupItemClick);

    // Add to container
    container.appendChild(listItem);
}


//end group append
function appendUserElement(chatEntry, container) {
    const { chatUser, lastMessage } = chatEntry;

    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.setAttribute('id', chatUser.userId);
    listItem.setAttribute('info', chatUser.username);

    const userImage = document.createElement('img');
    userImage.src = '../img/user_icon.png';
    userImage.alt = chatUser.username;

    const userInfoWrapper = document.createElement('div');
    userInfoWrapper.style.display = 'flex';
    userInfoWrapper.style.flexDirection = 'column';

    const usernames = document.createElement('span');
    usernames.textContent = chatUser.username;
    usernames.style.fontWeight = 'bold';

    const lastMessageText = document.createElement('span');
    lastMessageText.textContent = lastMessage ? lastMessage.content : 'No messages yet.';
    lastMessageText.classList.add('last-message');
    lastMessageText.style.fontSize = '0.85em';
    lastMessageText.style.color = '#eee';

    userInfoWrapper.appendChild(usernames);
    userInfoWrapper.appendChild(lastMessageText);

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(userInfoWrapper);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', userItemClick);

    container.appendChild(listItem);
}



function userItemClick(event) {
     selectedGroupId=null;
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectUsername=clickedUser.getAttribute('info');
    selectedUserId = clickedUser.getAttribute('id');
    fetchAndDisplayUserChat().then();



    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';
    unreadCount=0;


}

 async function fetchAndDisplayUserChat() {


    const userChatResponse = await fetch(`http://localhost:8087/api/chat/messages/${nickname}/${selectedUserId}`);
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.sender.username, chat.content);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}
function groupItemClick(event) {
selectUsername=null;

    // Remove 'active' from all group items
    document.querySelectorAll('.group-item').forEach(item => {
        item.classList.remove('active');
    });

    // Show the message form
    messageForm.classList.remove('hidden');

    const clickedGroup = event.currentTarget;
    clickedGroup.classList.add('active');

    // Set global group variables
    selectGroupName = clickedGroup.getAttribute('info');
    selectedGroupId = clickedGroup.getAttribute('id');
     const groupId = selectedGroupId.replace('group-', '');   // "1"
    // Fetch and display messages for the selected group
    fetchAndDisplayGroupChat(groupId).then();

    // Hide and reset unread message badge
    const nbrMsg = clickedGroup.querySelector('.nbr-msg');
    if (nbrMsg) {
        nbrMsg.classList.add('hidden');
        nbrMsg.textContent = '0';
    }

    unreadGroupCount = 0; // You can use a separate variable for group unread count if needed
}

function subscribeToGroup(groupid) {
    // Subscribe to the group's WebSocket topic
    stompClient.subscribe(`/topic/group-${groupid}`, onMessageReceivedGroup);

}


function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    if (senderId === nickname) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }
    const message = document.createElement('p');
    message.textContent = content;
    messageContainer.appendChild(message);
    chatArea.appendChild(messageContainer);
}

async function fetchAndDisplayGroupChat(groupId) {
    try {
        const groupChatResponse = await fetch(`http://localhost:8087/api/chat/group-messages/${groupId}`);
        const groupChat = await groupChatResponse.json();

        chatArea.innerHTML = ''; // Clear old messages

        groupChat.forEach(chat => {
            displayMessage(chat.senderUsername, chat.content);
        });

        chatArea.scrollTop = chatArea.scrollHeight;

    } catch (error) {
        console.error('Error fetching group chat:', error);
    }
}


function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        const chatMessage = {
            senderUsername: nickname,
            recipientUsername: selectUsername,
            content: messageInput.value.trim(),
            timestamp: new Date()
        };
        stompClient.send("/app/sendPrivateMessage", {}, JSON.stringify(chatMessage));
        displayMessage(nickname, messageInput.value.trim());
        messageInput.value = '';
    }
    chatArea.scrollTop = chatArea.scrollHeight;
    event.preventDefault();
}




async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers(); // Refresh the user list
    console.log('📩 Message received:', payload);

    const message = JSON.parse(payload.body);
    console.log("📢 Sender Username:", message.senderUsername);

    // Check if the message is from the currently selected user
    if (selectUsername && selectUsername === message.senderUsername) {
        displayMessage(message.senderUsername, message.content);
        console.log("✅ Message displayed in chat area.");
        chatArea.scrollTop = chatArea.scrollHeight; // Auto-scroll to latest message
        return; // Exit function since the message is already displayed
    }


    // Find the sender's list item in the user list
    const notifiedUser = document.querySelector(`[info="${message.senderUsername}"]`);

    if (notifiedUser) {
        console.log(`👤 Found notifiedUser:`, notifiedUser);

        // Find or create the notification badge
        let nbrMsg = notifiedUser.querySelector('.nbr-msg');

        if (!nbrMsg) {
            console.warn("⚠️ .nbr-msg not found, creating it...");
            nbrMsg = document.createElement('span');
            nbrMsg.classList.add('nbr-msg');
            notifiedUser.appendChild(nbrMsg);
        }

        // Get the current count
        unreadCount += 1; // Increment unread count

        console.log(` New count: ${unreadCount}`);

        // Update the UI
        nbrMsg.textContent = unreadCount;
        nbrMsg.classList.remove('hidden'); // Ensure visibility
    } else {
        console.warn(`🚨 No user found with info="${message.senderUsername}"`);
    }
}


async function onMessageReceivedGroup(payload) { // Refresh the user list
    await findAndDisplayUserGroups(); // Refresh the group list
    console.log('📩 Message received:', payload);

    const message = JSON.parse(payload.body);
    console.log("📢 Sender Username:", message.senderUsername);

    console.log("Selected Group ID: " + selectedGroupId); // Logging the selected group ID for debugging

    // Ensure the sender's message is not displayed on the sender's side
    if (message.senderUsername === nickname) {
        console.log("⛔ Skip displaying message from the sender.");
        return; // Skip displaying the message if it was sent by the current user
    }

    // Check if selectedGroupId is valid and defined
    if (selectedGroupId) {
        const selectgroupid = selectedGroupId.replace("group-", ""); // Clean up the group ID
        console.log("Processed Group ID: " + selectgroupid);

        // Handle group message based on group ID
        if (selectgroupid === message.groupId.toString()) {

            console.log("the message id is "+  message.groupId.toString());
            // If the message is from the selected group, display it in the group chat
            displayMessage(message.senderUsername, message.content);
            console.log("✅ Message displayed in group chat area.");
            chatArea.scrollTop = chatArea.scrollHeight; // Auto-scroll to the latest message
        } else {
            // If it's not from the selected group, handle the unread message count
            const groupElement = document.querySelector(`[group="${message.groupId}"]`);

            if (groupElement) {
                console.log(`👥 Found group: ${message.groupId}`);

                // Find or create the notification badge for the group
                let groupNbrMsg = groupElement.querySelector('.nbr-msg');

                if (!groupNbrMsg) {
                    console.warn("⚠️ .nbr-msg not found for group, creating it...");
                    groupNbrMsg = document.createElement('span');
                    groupNbrMsg.classList.add('nbr-msg');
                    groupElement.appendChild(groupNbrMsg);
                }

                // Increment unread count for group
                unreadGroupCount += 1; // Increment unread group count
                console.log(` New group unread count: ${unreadGroupCount}`);

                // Update the UI
                groupNbrMsg.textContent = unreadGroupCount;
                groupNbrMsg.classList.remove('hidden'); // Ensure visibility
            } else {
                console.warn(`🚨 No group found with group="${message.groupId}"`);
            }
        }
    } else {
        console.error("🚨 No selected group ID found. Unable to process the message.");
    }
}




function sendGroupMessage(event) {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
   const groupMessage = {
       senderUsername: nickname,
       groupId: selectedGroupId.replace('group-', ''), // Remove the 'group-' prefix to send only the numeric part
       content: messageInput.value.trim(),
       timestamp: new Date()
   };

        // Sending the message to the group
        stompClient.send("/app/sendGroupMessage", {}, JSON.stringify(groupMessage));

        // Optionally display the sent message immediately in the chat area
        displayMessage(nickname, messageInput.value.trim());

        // Clear the input after sending the message
        messageInput.value = '';
    }

    // Scroll to the latest message
    chatArea.scrollTop = chatArea.scrollHeight;

    event.preventDefault();
}

function sendMessageBasedOnContext(event) {
    const messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        if (selectedGroupId) {
            // Send Group Message
            sendGroupMessage(event);
        } else if (selectUsername) {
            // Send Private Message
            sendMessage(event);
        }
    }

    event.preventDefault();
}




function onLogout() {
    stompClient.send("/app/user.disconnectUser",
        {},
        JSON.stringify({nickName: nickname, fullName: fullname, status: 'OFFLINE'})
    );
    window.location.reload();
}

messageForm.addEventListener('submit', sendMessageBasedOnContext, true);
logout.addEventListener('click', onLogout, true);
window.onbeforeunload = () => onLogout();
