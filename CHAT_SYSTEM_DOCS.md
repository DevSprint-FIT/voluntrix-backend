# Real-Time Chat System Documentation

## Overview
This is a WhatsApp-like real-time chat system built with Spring Boot WebSockets, providing instant messaging, user presence tracking, typing indicators, and message status tracking.

## Features Implemented

### 🚀 Core Features
- **Real-time messaging** with instant delivery
- **User presence tracking** (online/offline status)
- **Typing indicators** ("User is typing..." notifications)
- **Message status tracking** (sent, delivered, read)
- **Chat history persistence** in PostgreSQL database
- **Multiple device support** (same user can connect from multiple browsers)
- **WhatsApp-like UI** with modern design

### 🛠 Technical Components

#### 1. WebSocket Configuration (`WebSocketConfig.java`)
- Configures STOMP messaging over WebSocket
- Enables message broker for topic-based messaging
- Sets up endpoints for WebSocket connections

#### 2. Enhanced Message Entity (`Message.java`)
- Extended with message status, type, and timestamps
- Supports different message types (CHAT, FILE, IMAGE, etc.)
- Tracks delivery and read timestamps

#### 3. Real-time Controllers
- **`ChatWebSocketController.java`**: Handles real-time WebSocket messages
- **`ChatRestController.java`**: Provides REST APIs for chat history and user management

#### 4. Core Services
- **`ChatService.java`**: Manages message persistence and retrieval
- **`UserSessionService.java`**: Tracks online users and session management

#### 5. WebSocket Event Listener (`WebSocketEventListener.java`)
- Handles user connect/disconnect events
- Manages user presence broadcasting

## API Endpoints

### WebSocket Endpoints
- **Connection**: `ws://localhost:8080/ws`
- **Send Message**: `/app/chat.sendMessage`
- **Add User**: `/app/chat.addUser`
- **Typing**: `/app/chat.typing`
- **Stop Typing**: `/app/chat.stopTyping`
- **Mark as Read**: `/app/chat.markAsRead`

### WebSocket Topics (Subscriptions)
- **Private Messages**: `/user/topic/messages`
- **Message Status**: `/user/topic/message-status`
- **Typing Indicators**: `/user/topic/typing`
- **Public Messages**: `/topic/public`
- **User Status**: `/topic/user-status`

### REST API Endpoints
- **GET** `/api/chat/history?user1=X&user2=Y` - Get chat history between two users
- **GET** `/api/chat/unread/{userId}` - Get unread messages for a user
- **POST** `/api/chat/mark-read/{messageId}` - Mark message as read
- **GET** `/api/chat/online-users` - Get list of online users
- **GET** `/api/chat/user-status/{userId}` - Get specific user's status

## How to Use

### 1. Start the Application
```bash
./mvnw spring-boot:run
```

### 2. Open the Chat Interface
Navigate to: `http://localhost:8080/chat.html`

### 3. Test Real-time Chat
1. **Open multiple browser tabs/windows**
2. **Enter different User IDs and Names** in each tab
3. **Click "Connect"** to establish WebSocket connection
4. **Set Receiver ID** to chat with specific users
5. **Send messages** and see real-time delivery
6. **Observe typing indicators** when users type
7. **Check online users** list updates automatically

### 4. Testing Scenarios
- **Multi-user chat**: Open 3+ tabs with different users
- **Cross-tab messaging**: Send messages between different browser tabs
- **Typing indicators**: Start typing and see indicators in other tabs
- **Connection status**: Connect/disconnect and observe status changes
- **Chat history**: Reload page and see message history preserved

## Database Schema

### Message Table
```sql
CREATE TABLE message (
    id BIGSERIAL PRIMARY KEY,
    sender_id VARCHAR(255) NOT NULL,
    receiver_id VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    sender_name VARCHAR(255),
    timestamp TIMESTAMP NOT NULL,
    type VARCHAR(50) DEFAULT 'CHAT',
    status VARCHAR(50) DEFAULT 'SENT',
    delivered_at TIMESTAMP,
    read_at TIMESTAMP
);
```

## Frontend Features

### UI Components
- **Modern WhatsApp-like design** with green color scheme
- **Connection status indicator**
- **Online users display**
- **Chat message bubbles** (sent/received styling)
- **Typing indicators**
- **Message timestamps**
- **Real-time updates**

### JavaScript Functionality
- **STOMP client** for WebSocket communication
- **SockJS fallback** for connection reliability
- **Automatic reconnection handling**
- **Real-time UI updates**
- **Message status tracking**

## Advanced Features

### 1. Message Status Tracking
- **SENT**: Message sent by sender
- **DELIVERED**: Message delivered to receiver
- **READ**: Message read by receiver

### 2. User Presence System
- **Online**: User actively connected
- **Offline**: User disconnected
- **Last seen**: Timestamp of last activity

### 3. Multi-device Support
- Users can connect from multiple devices
- All devices receive messages simultaneously
- Proper session management

### 4. Typing Indicators
- Real-time typing notifications
- Auto-stop typing after inactivity
- Per-conversation typing status

## Production Considerations

### Security
- Add authentication/authorization
- Validate message content
- Rate limiting for message sending
- CORS configuration for specific origins

### Scalability
- Use Redis for session management
- Implement message queuing (RabbitMQ/Apache Kafka)
- Database optimization and indexing
- Horizontal scaling with load balancers

### Performance
- Message pagination for large chat histories
- File upload support for media messages
- Caching frequently accessed data
- Connection pooling optimization

## Troubleshooting

### Common Issues
1. **Connection Failed**: Check if application is running on port 8080
2. **Database Errors**: Ensure PostgreSQL is running and configured
3. **Messages Not Delivering**: Check WebSocket connection status
4. **CORS Issues**: Update WebSocket configuration for production

### Debugging
- Check browser console for WebSocket errors
- Monitor application logs for connection issues
- Use browser developer tools to inspect WebSocket traffic

## Next Steps

### Potential Enhancements
1. **File sharing** (images, documents, videos)
2. **Group chat** functionality
3. **Message encryption** for security
4. **Push notifications** for offline users
5. **Message search** and filtering
6. **User profiles** and avatars
7. **Message reactions** (like, dislike, emoji)
8. **Voice messages** support

### Integration Options
- **Mobile app** (React Native, Flutter)
- **Desktop app** (Electron)
- **Email notifications** for offline messages
- **Social media integration**

---

## Summary

You now have a fully functional, WhatsApp-like real-time chat system with:
- ✅ Real-time messaging via WebSockets
- ✅ User presence tracking
- ✅ Typing indicators
- ✅ Message status (sent/delivered/read)
- ✅ Chat history persistence
- ✅ Modern web interface
- ✅ Multi-user support
- ✅ Database integration

The system is production-ready with proper architecture and can be extended with additional features as needed!
