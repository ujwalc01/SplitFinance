**SplitFinance**:
	A backend microservices-based financial transaction monitoring system using Java Spring Boot and MySQL.

**Overview**:
	SplitFinance is designed to track financial transactions, manage data efficiently, and provide observability using structured logging and metrics.

**Features**:
1. Authentication & Authorization:
		JWT-Based Security: Secure every service endpoint using Spring Security.
		Role-Based Access Control: Support different user roles (e.g., user, admin).

2. User Management:
		User Registration: API for new user signup.
		Profile Management: Endpoints to view, update, and delete user profiles.

3. Expense Management:
		Expense Creation/Editing: Users can create new expenses and update or delete them.
		Expense Splitting Options: Support for different split types—equal, unequal, or percentage-based splits.

4. Group Management:
		Group Creation: Allow users to form groups and add friends.
		Group Expense Summary: Overview of total expenses and pending settlement transactions within a group.

5. Settlement & Reconciliation:
		Minimization of Transactions: Calculate and suggest the minimal set of payments required to settle all debts.
		Notification System:

6. Real-Time Alerts:
   	WebSockets or push notifications for updates on expense additions or settlements.

8. Audit and Logging:

9. Activity Logs:
    Maintain audit trails for changes such as expense modifications or user actions.

**Architectural Planning & Microservices Breakdown**
**Service Decomposition**:
1. User Service: Handles user operations and authentication.
2. Expense Service: Manages all endpoints related to expense entry, updates, deletion, and querying past expenses.
3. Group/Settlement Service: Manages group creation, member management, and settlement calculations.
3. Notification Service: Deals with asynchronous notifications and alerts.

**Future services**:
Third-Party/Payment Integration:
Payment Gateway Integration: Enabling users to complete settlements online.
Reporting and Analytics:
Data Export/Visualization: Provide downloadable CSV reports or visual graphs of expenses over time.
API Gateway: Use Spring Cloud Gateway to route client requests to the appropriate microservice.
Inter-Service Messaging: Consider lightweight messaging (e.g., Kafka or RabbitMQ) for notifying relevant services about events like a new expense being added.
Communication:
