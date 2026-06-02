package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.drawscope.translate
import com.athena.athenaapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.util.Log

// Define Screen Navigation states representing clean onboarding flow
enum class AuthScreen {
    WELCOME,                // Welcome to Athena Page
    LOGIN_SIGNUP,           // Login / Create Account Form
    EMAIL_VERIFICATION,     // Screen 3: Verify email before proceeding
    ORGANIZATION_SETUP,     // Screen 4: Select Organization Type
    PROFILE_SETUP,          // Step 3: Setup Profile Details (Screens 5-7)
    AUTHENTICATED           // Role-Specific Premium Interactive Dashboard
}

// Brand Color Palette conforming to Design guidelines (Warm Organic and Premium Slate accents)
val PureWhite = Color(0xFFFFFFFF)
val OffWhite = Color(0xFFD1D0E3) // Same Lavender background matching welcome screen
val Lavender = Color(0xFFD1D0E3) // Custom brand Lavender: #D1D0E3
val MossGreen = Color(0xFF111827) // Solid Black matching welcome screen buttons
val MossGreenDark = Color(0xFF111827) // Solid Black
val MossGreenLight = Color(0xFF4B5563) // Medium Grey
val SoftDark = Color(0xFF111827) // Solid Black
val TextGray = Color(0xFF374151) // Dark Grey for readable body text
val BorderGray = Color(0x26111827) // Light black (approx 15% opacity) for subtle borders
val GoldOpportunity = Color(0xFFB45309) // High-contrast opportunity color
val SoftBlueCard = Color(0xFFFFFFFF) // High-contrast clean white card matching login page
val BlueAccent = Color(0xFF111827) // Solid Black

// Custom App Theme
@Composable
fun AthenaTheme(content: @Composable () -> Unit) {
    val lightColors = lightColorScheme(
        primary = Color(0xFF111827),
        onPrimary = PureWhite,
        primaryContainer = Lavender,
        secondary = Color(0xFF111827),
        background = Lavender,
        surface = PureWhite,
        onBackground = Color(0xFF111827),
        onSurface = Color(0xFF111827)
    )

    val typography = Typography(
        displayLarge = TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = (-1).sp
        ),
        titleLarge = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.SansSerif
        ),
        bodyLarge = TextStyle(
            fontSize = 15.sp,
            fontFamily = FontFamily.SansSerif,
            lineHeight = 22.sp
        )
    )

    MaterialTheme(
        colorScheme = lightColors,
        typography = typography,
        content = content
    )
}

// Global Simulated Data Models representing complete Organization Architecture
data class UserAccount(
    val name: String,
    val email: String,
    val role: String, // "Procurement Manager", "Supplier", "Organization Admin", "Executive"
    val companyName: String = "",
    val department: String = "Operations",
    val password: String = "password"
)

data class DashboardItem(
    val id: String, 
    val title: String, 
    val status: String, 
    val extra: String,
    val department: String = "Infrastructure",
    val budgetValue: String = "$100,000"
)

data class OpportunityItem(
    val id: String,
    val title: String,
    val orgName: String,
    val value: String,
    val location: String,
    val category: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AthenaTheme {
                WelcomeScreen()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WelcomeScreen() {
    var currentScreen by remember { mutableStateOf(AuthScreen.WELCOME) }
    var initialAuthTab by remember { mutableStateOf(0) } // 0 = Sign In, 1 = Sign Up

    // Onboarding Account Details Cache
    var signupName by remember { mutableStateOf("") }
    var signupEmail by remember { mutableStateOf("") }
    var signupPassword by remember { mutableStateOf("") }
    var selectedOrgType by remember { mutableStateOf("") } // "Construction" or "Supplier"

    // Company profile metadata values
    var computedCompanyName by remember { mutableStateOf("") }
    var computedProductsOrIndustry by remember { mutableStateOf("") }
    var computedLocationOrSize by remember { mutableStateOf("") }

    // Persistent simulation database states
    val registeredAccounts = remember {
        mutableStateListOf(
            UserAccount("Alex Admin", "admin@athena.com", "Organization Admin", "Athena Builders Corp"),
            UserAccount("Mark Manager", "manager@athena.com", "Procurement Manager", "Athena Builders Corp", "Procurement Operations"),
            UserAccount("Elena Exec", "exec@athena.com", "Executive", "Athena Builders Corp", "Strategic Financing"),
            UserAccount("Sam Supplier", "supplier@athena.com", "Supplier", "Apex Industrial Minerals", "Trade Operations")
        )
    }

    var currentUserAccount by remember {
        mutableStateOf(registeredAccounts[1]) // Default landing with Procurement Manager
    }

    var isSandboxMode by remember { mutableStateOf(true) }
    val context = LocalContext.current

    // Safely check Firebase availability
    var checkFirebaseAvailable by remember { mutableStateOf(false) }
    var firebaseAuthInstance: FirebaseAuth? = null
    var firebaseDbInstance: FirebaseDatabase? = null

    try {
        firebaseAuthInstance = FirebaseAuth.getInstance()
        checkFirebaseAvailable = firebaseAuthInstance != null
        Log.d("FirebaseSetup", "FirebaseAuth successfully initialized")
    } catch (e: Exception) {
         Log.e("FirebaseSetup", "FirebaseAuth initialization failed: ${e.message}")
         checkFirebaseAvailable = false
    }

    try {
        firebaseDbInstance = FirebaseDatabase.getInstance()
        Log.d("FirebaseSetup", "FirebaseDatabase successfully initialized with default instance")
    } catch (e: Exception) {
         try {
             // Highly robust explicit default fallback
             firebaseDbInstance = FirebaseDatabase.getInstance("https://athena-3d80c-default-rtdb.firebaseio.com/")
             Log.d("FirebaseSetup", "FirebaseDatabase successfully initialized with fallback URL")
         } catch (eInner: Exception) {
             Log.e("FirebaseSetup", "FirebaseDatabase initialization failed: ${eInner.message}")
         }
    }

    // Dynamic state collection matching sovereign organization workflows
    val rfqList = remember {
        mutableStateListOf(
            DashboardItem("RFQ-2026-X9", "Sovereign Structural Grain Stockpile", "Active", "Pending supplier tenders", "Material Supply", "$95,000"),
            DashboardItem("RFQ-2026-C4", "Standard Carbon Alloy Rails", "Matching", "3 bids under audit", "Civil Rails", "$240,000"),
            DashboardItem("RFQ-2026-F1", "Liquid Phosphate Barrel Grade", "Completed", "Awarded to Apex Minerals", "Agricultural", "$65,000")
        )
    }

    val simulatedOpportunities = remember {
        mutableStateListOf(
            OpportunityItem("OPP-301", "Heavy Reinforcement Gaskets", "Athena Builders Corp", "$32,500/lot", "Central Depot HUB", "Hardware"),
            OpportunityItem("OPP-302", "Grade 4 Portland Hydraulic Cement", "Athena Builders Corp", "$94,000/tender", "Pacific Terminal Dock", "Foundations"),
            OpportunityItem("OPP-303", "Precision Alloy Girders and Rails", "Global Rails Inc", "$150,000/unit", "East Corridor Transit", "Steel Products"),
            OpportunityItem("OPP-304", "Structural Grade Aggregate Mix", "Athena Builders Corp", "$18,500/delivery", "Sector 9 Depot", "Foundations")
        )
    }

    val appliedBids = remember {
        mutableStateListOf<String>() // tracks Opportunity IDs bid on by supplier
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Lavender)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    if (targetState.ordinal > initialState.ordinal) {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> -width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> width } + fadeOut()
                    }
                },
                label = "athena_screen_lifecycle"
            ) { screen ->
                when (screen) {
                    AuthScreen.WELCOME -> {
                        LandingScreenContent(
                            innerPadding = innerPadding,
                            onSignUpClick = {
                                initialAuthTab = 1
                                currentScreen = AuthScreen.LOGIN_SIGNUP
                            },
                            onSignInClick = {
                                initialAuthTab = 0
                                currentScreen = AuthScreen.LOGIN_SIGNUP
                            }
                        )
                    }
                    AuthScreen.LOGIN_SIGNUP -> {
                        LoginSignUpScreenContent(
                            innerPadding = innerPadding,
                            firebaseAuth = firebaseAuthInstance,
                            firebaseDb = firebaseDbInstance,
                            isFirebaseAvailable = checkFirebaseAvailable,
                            registeredAccounts = registeredAccounts,
                            initialTab = initialAuthTab,
                            onBackToLanding = {
                                currentScreen = AuthScreen.WELCOME
                            },
                            onLoginSuccess = { user ->
                                currentUserAccount = user
                                currentScreen = AuthScreen.AUTHENTICATED
                                Toast.makeText(context, "Logged in as ${user.name} (${user.role})", Toast.LENGTH_SHORT).show()
                            },
                            onStep1Completed = { name, email, pass ->
                                signupName = name
                                signupEmail = email
                                signupPassword = pass
                                currentScreen = AuthScreen.EMAIL_VERIFICATION
                            }
                        )
                    }
                    AuthScreen.EMAIL_VERIFICATION -> {
                        EmailVerificationScreen(
                            innerPadding = innerPadding,
                            email = signupEmail,
                            onEmailChanged = { signupEmail = it },
                            onBack = { currentScreen = AuthScreen.LOGIN_SIGNUP },
                            onVerified = {
                                currentScreen = AuthScreen.ORGANIZATION_SETUP
                            }
                        )
                    }
                    AuthScreen.ORGANIZATION_SETUP -> {
                        OrganizationSetupScreen(
                            innerPadding = innerPadding,
                            onBack = { currentScreen = AuthScreen.LOGIN_SIGNUP },
                            onNext = { orgType ->
                                selectedOrgType = orgType
                                currentScreen = AuthScreen.PROFILE_SETUP
                            }
                        )
                    }
                    AuthScreen.PROFILE_SETUP -> {
                        ProfileSetupScreen(
                            innerPadding = innerPadding,
                            orgType = selectedOrgType,
                            onBack = { currentScreen = AuthScreen.ORGANIZATION_SETUP },
                            onFinishOnboarding = { compName, productInfo, locatorInfo, extraInvites ->
                                computedCompanyName = compName
                                computedProductsOrIndustry = productInfo
                                computedLocationOrSize = locatorInfo

                                val mappedRole = if (selectedOrgType == "Supplier") "Supplier" else "Organization Admin"
                                val newAccount = UserAccount(
                                    name = signupName.ifEmpty { "New User" },
                                    email = signupEmail,
                                    role = mappedRole,
                                    companyName = compName,
                                    department = if (mappedRole == "Supplier") "Supply Logistics" else "Global Administration",
                                    password = signupPassword
                                )

                                // Add account into live credentials index
                                registeredAccounts.add(newAccount)
                                currentUserAccount = newAccount

                                // Plus add extra invited team members if any
                                extraInvites.forEach { invite ->
                                    val passwordSimulated = "athena123"
                                    val invitedUser = UserAccount(
                                        name = invite.name,
                                        email = invite.email,
                                        role = invite.role,
                                        companyName = compName,
                                        department = invite.department,
                                        password = passwordSimulated
                                    )
                                    registeredAccounts.add(invitedUser)

                                    if (checkFirebaseAvailable && firebaseAuthInstance != null) {
                                        firebaseDbInstance?.getReference("users")?.child(invite.email.replace(".", "_"))?.setValue(invitedUser)
                                    }
                                }

                                // If Firebase connected, update records
                                if (checkFirebaseAvailable && firebaseAuthInstance != null) {
                                    firebaseAuthInstance.createUserWithEmailAndPassword(signupEmail, signupPassword)
                                        .addOnSuccessListener { res ->
                                            val uid = res.user?.uid
                                            if (uid != null) {
                                                firebaseDbInstance?.getReference("users")?.child(uid)?.setValue(newAccount)
                                            }
                                        }
                                }

                                currentScreen = AuthScreen.AUTHENTICATED
                                Toast.makeText(context, "Sovereign Ledger Node Established with ${extraInvites.size} team members!", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                    AuthScreen.AUTHENTICATED -> {
                        AuthenticatedDashboardScreen(
                            innerPadding = innerPadding,
                            user = currentUserAccount,
                            isSandbox = isSandboxMode,
                            rfqList = rfqList,
                            opportunitiesList = simulatedOpportunities,
                            appliedBids = appliedBids,
                            registeredAccounts = registeredAccounts,
                            onSwitchRoleSimulated = { swappedUser ->
                                currentUserAccount = swappedUser
                                Toast.makeText(context, "Viewing Workspace clearance as ${swappedUser.role}", Toast.LENGTH_SHORT).show()
                            },
                            onAddPersonnel = { userName, userEmail, userRole, dept ->
                                val addedUser = UserAccount(userName, userEmail, userRole, currentUserAccount.companyName, dept)
                                registeredAccounts.add(addedUser)
                                Toast.makeText(context, "$userName successfully deployed to roster.", Toast.LENGTH_SHORT).show()
                            },
                            onLogOutClick = {
                                try {
                                    firebaseAuthInstance?.signOut()
                                } catch (e: Exception) {}
                                currentScreen = AuthScreen.WELCOME
                            }
                        )
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// 1. WELCOME LANDING SCREEN (HIGH FIDELITY OVERHAUL)
// ----------------------------------------------------
@Composable
fun LandingScreenContent(
    innerPadding: PaddingValues,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Lavender) // Soft periwinkle lavender background color (#D1D0E3)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Brand Header Row (matching 'rooms' top left layout)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo image used for top-left brand header
            androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.athena_logo),
                contentDescription = "Athena brand header logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(32.dp) // Elegant compact size for header brand mark
                    .testTag("app_brand_title")
            )
        }

        // Center Graphic Area with a BIG transparent logo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2.2f) // Significantly larger weight to give the logo maximum layout presence
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            // Prominent, transparent-background central Athena logo - rendered BIG as requested
            Box(
                modifier = Modifier
                    .fillMaxSize() // Fills the entire massive weighted area
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.athena_logo),
                    contentDescription = "Athena Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("onboarding_logo")
                )
            }
        }

        // Beautiful Typographic Main Section matching the elegant font layout
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Line 1: procure materials
            Text(
                text = "procure materials",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF111827), // Black text font color
                letterSpacing = (-1).sp
            )

            // Line 2: directly with
            Text(
                text = "directly with",
                fontSize = 32.sp,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF111827).copy(alpha = 0.65f), // Matching lighter elegant theme styling using black alpha
                letterSpacing = (-0.5).sp
            )

            // Line 3: verified suppliers
            Text(
                text = "verified suppliers",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF111827), // Black text font color
                letterSpacing = (-1).sp
            )
        }

        // Primary & Secondary Action Button Container matching mockup layout (rounded capsule pills)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Primary High-contrast Solid Black Capsule Button
            Button(
                onClick = onSignUpClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF111827), // Solid black container matching the black logo aesthetic
                    contentColor = PureWhite
                ),
                shape = CircleShape, // Perfectly round capsule pills
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("get_started_btn")
            ) {
                Text(
                    text = "Get started",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.3.sp
                )
            }

            // Secondary Outlined Glassy Button matching 'I have an account' mockup layout
            Button(
                onClick = onSignInClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.35f),
                    contentColor = Color(0xFF111827)
                ),
                border = BorderStroke(1.5.dp, Color(0xFF111827).copy(alpha = 0.4f)),
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("have_account_btn")
            ) {
                Text(
                    text = "I already have an account",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.3.sp
                )
            }
        }

        // Disclaimer statement at the bottom
        Text(
            text = "By continuing you confirm that you agree to our Terms of Service, Global Procurement Policies, and Secure Platform Conduct.",
            fontSize = 11.sp,
            color = Color(0xFF111827).copy(alpha = 0.65f),
            lineHeight = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 16.dp)
        )
    }
}

// ----------------------------------------------------
// 2. SIGN IN & SIGN UP (TABBED SCREEN)
// ----------------------------------------------------
@Composable
fun LoginSignUpScreenContent(
    innerPadding: PaddingValues,
    firebaseAuth: FirebaseAuth?,
    firebaseDb: FirebaseDatabase?,
    isFirebaseAvailable: Boolean,
    registeredAccounts: List<UserAccount>,
    initialTab: Int = 0,
    onBackToLanding: () -> Unit,
    onLoginSuccess: (UserAccount) -> Unit,
    onStep1Completed: (String, String, String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(initialTab) }
    LaunchedEffect(initialTab) {
        selectedTab = initialTab
    }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackToLanding,
                modifier = Modifier
                    .size(40.dp)
                    .background(PureWhite, CircleShape)
                    .border(1.dp, BorderGray, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Return home",
                    tint = Color(0xFF111827)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Athena Secure Portal",
                fontSize = 14.sp,
                color = Color(0xFF111827).copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = PureWhite.copy(alpha = 0.95f)
                ),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFF111827).copy(alpha = 0.15f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (selectedTab == 0) "Welcome Back" else "Create Account",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF111827),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = if (selectedTab == 0) 
                                "Enter credentials to reach your sovereign dashboard." 
                            else 
                                "Step 1: Enter your core personal details.",
                            fontSize = 13.sp,
                            color = Color(0xFF111827).copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                        )
                    }

                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Lavender.copy(alpha = 0.3f),
                        contentColor = Color(0xFF111827),
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = Color(0xFF111827),
                                height = 3.dp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Sign In", fontWeight = FontWeight.Bold, fontSize = 14.sp) },
                            modifier = Modifier.testTag("tab_sign_in")
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Sign Up", fontWeight = FontWeight.Bold, fontSize = 14.sp) },
                            modifier = Modifier.testTag("tab_sign_up")
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    if (selectedTab == 0) {
                        SignInForm(
                            firebaseAuth = firebaseAuth,
                            isFirebaseAvailable = isFirebaseAvailable,
                            registeredAccounts = registeredAccounts,
                            onLoginSuccess = onLoginSuccess
                        )
                    } else {
                        SignUpFormStep1(
                            onContinue = onStep1Completed
                        )
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// 2A. TAB: SIGN IN FORM
// ----------------------------------------------------
@Composable
fun SignInForm(
    firebaseAuth: FirebaseAuth?,
    isFirebaseAvailable: Boolean,
    registeredAccounts: List<UserAccount>,
    onLoginSuccess: (UserAccount) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Simulation Sandbox quick credentials logs helper with matched styling
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF111827).copy(alpha = 0.05f)),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Color(0xFF111827).copy(alpha = 0.1f)),
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "🔐 Demo Workspace Simulation Login Panel",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "Click below to log in as any role immediately for product evaluation:",
                    fontSize = 10.5.sp,
                    color = Color(0xFF111827).copy(alpha = 0.65f),
                    modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val quickRoles = listOf(
                        "Manager" to ("manager@athena.com" to "password"),
                        "Supplier" to ("supplier@athena.com" to "password"),
                        "Admin" to ("admin@athena.com" to "password"),
                        "Exec" to ("exec@athena.com" to "password")
                    )
                    quickRoles.forEach { (role, creds) ->
                        Button(
                            onClick = {
                                email = creds.first
                                password = creds.second
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF111827).copy(alpha = 0.12f),
                                contentColor = Color(0xFF111827)
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(30.dp).weight(1f)
                        ) {
                            Text(role, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorMessage = "" },
            label = { Text("Email Address") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Email", tint = Color(0xFF111827).copy(alpha = 0.6f))
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF111827),
                unfocusedBorderColor = BorderGray,
                unfocusedContainerColor = PureWhite,
                focusedContainerColor = PureWhite,
                focusedLabelColor = Color(0xFF111827),
                unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth().testTag("signin_email")
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = "" },
            label = { Text("Password") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Pass Lock", tint = Color(0xFF111827).copy(alpha = 0.6f))
            },
            trailingIcon = {
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(
                        text = if (passwordVisible) "HIDE" else "SHOW",
                        color = Color(0xFF111827),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF111827),
                unfocusedBorderColor = BorderGray,
                unfocusedContainerColor = PureWhite,
                focusedContainerColor = PureWhite,
                focusedLabelColor = Color(0xFF111827),
                unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().testTag("signin_password")
        )

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    errorMessage = "Both Email and Password are required."
                    return@Button
                }
                isLoading = true
                errorMessage = ""

                val matchedAccount = registeredAccounts.find { it.email.trim().lowercase() == email.trim().lowercase() }
                
                if (isFirebaseAvailable && firebaseAuth != null) {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            isLoading = false
                            if (matchedAccount != null && matchedAccount.password == password) {
                                onLoginSuccess(matchedAccount)
                            } else {
                                onLoginSuccess(UserAccount("Verified Member", email, "Procurement Manager", password = password))
                            }
                        }
                        .addOnFailureListener { err ->
                            isLoading = false
                            if (matchedAccount != null && matchedAccount.password == password) {
                                onLoginSuccess(matchedAccount)
                            } else {
                                errorMessage = err.localizedMessage ?: "Invalid verification credentials."
                            }
                        }
                } else {
                    isLoading = false
                    if (matchedAccount != null) {
                        if (matchedAccount.password == password) {
                            onLoginSuccess(matchedAccount)
                        } else {
                            errorMessage = "Incorrect password. Please try again."
                        }
                    } else {
                        errorMessage = "Incorrect email/password, or account does not exist."
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF111827),
                contentColor = PureWhite
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_signin_btn")
        ) {
            Text(
                text = if (isLoading) "ESTABLISHING CLEARANCE..." else "Sign In",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

// ----------------------------------------------------
// 2B. TAB: SIGN UP STEP 1 (Create Account)
// ----------------------------------------------------
@Composable
fun SignUpFormStep1(
    onContinue: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it; errorMessage = "" },
            label = { Text("Full Name") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Name icon", tint = Color(0xFF111827).copy(alpha = 0.6f))
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF111827),
                unfocusedBorderColor = BorderGray,
                unfocusedContainerColor = PureWhite,
                focusedContainerColor = PureWhite,
                focusedLabelColor = Color(0xFF111827),
                unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
            ),
            modifier = Modifier.fillMaxWidth().testTag("signup_name")
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorMessage = "" },
            label = { Text("Email Address") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Email icon", tint = Color(0xFF111827).copy(alpha = 0.6f))
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF111827),
                unfocusedBorderColor = BorderGray,
                unfocusedContainerColor = PureWhite,
                focusedContainerColor = PureWhite,
                focusedLabelColor = Color(0xFF111827),
                unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth().testTag("signup_email")
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = "" },
            label = { Text("Create Password (min. 6 chars)") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock icon", tint = Color(0xFF111827).copy(alpha = 0.6f))
            },
            trailingIcon = {
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(
                        text = if (passwordVisible) "HIDE" else "SHOW",
                        color = Color(0xFF111827),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF111827),
                unfocusedBorderColor = BorderGray,
                unfocusedContainerColor = PureWhite,
                focusedContainerColor = PureWhite,
                focusedLabelColor = Color(0xFF111827),
                unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().testTag("signup_password")
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it; errorMessage = "" },
            label = { Text("Confirm Password") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Confirm key lock", tint = Color(0xFF111827).copy(alpha = 0.6f))
            },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF111827),
                unfocusedBorderColor = BorderGray,
                unfocusedContainerColor = PureWhite,
                focusedContainerColor = PureWhite,
                focusedLabelColor = Color(0xFF111827),
                unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().testTag("signup_confirm")
        )

        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = {
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    errorMessage = "All registration fields are required."
                    return@Button
                }
                if (password.length < 6) {
                    errorMessage = "Password requires at least 6 characters."
                    return@Button
                }
                if (password != confirmPassword) {
                    errorMessage = "Password credentials confirmation mismatch."
                    return@Button
                }
                onContinue(name, email, password)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111827)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("signup_step1_btn")
        ) {
            Text("Continue to Step 2", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}

// ----------------------------------------------------
// 2C. SCREEN 3: EMAIL VERIFICATION
// ----------------------------------------------------
@Composable
fun EmailVerificationScreen(
    innerPadding: PaddingValues,
    email: String,
    onEmailChanged: (String) -> Unit,
    onBack: () -> Unit,
    onVerified: () -> Unit
) {
    var emailInput by remember { mutableStateOf(email) }
    var isEditing by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    
    LaunchedEffect(toastMessage) {
        if (toastMessage.isNotEmpty()) {
            kotlinx.coroutines.delay(2500)
            toastMessage = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .background(PureWhite, CircleShape)
                    .border(1.dp, BorderGray, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Return to signup",
                    tint = Color(0xFF111827)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Screen 3: Email Verification",
                fontSize = 14.sp,
                color = Color(0xFF111827).copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = PureWhite.copy(alpha = 0.95f)
                ),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFF111827).copy(alpha = 0.15f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Verify your email to continue",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF111827),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "A secure verification code has been dispatched to establish credentials clearance.",
                            fontSize = 13.sp,
                            color = Color(0xFF111827).copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                        )
                    }

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Lavender.copy(alpha = 0.25f)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (!isEditing) {
                                Text(
                                    text = email,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF111827),
                                    textAlign = TextAlign.Center
                                )
                                TextButton(
                                    onClick = { isEditing = true },
                                    modifier = Modifier.heightIn(min = 48.dp)
                                ) {
                                    Text(
                                        text = "Change email address",
                                        color = Color(0xFF111827),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        textDecoration = TextDecoration.Underline
                                    )
                                }
                            } else {
                                OutlinedTextField(
                                    value = emailInput,
                                    onValueChange = { emailInput = it },
                                    label = { Text("Update Work Email") },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF111827),
                                        unfocusedBorderColor = BorderGray,
                                        unfocusedContainerColor = PureWhite,
                                        focusedContainerColor = PureWhite
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    TextButton(
                                        onClick = { isEditing = false },
                                        modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                                    ) {
                                        Text("Cancel", color = Color(0xFF111827).copy(alpha = 0.6f))
                                    }
                                    Button(
                                        onClick = {
                                            if (emailInput.isNotEmpty()) {
                                                onEmailChanged(emailInput)
                                                isEditing = false
                                                toastMessage = "Email updated successfully"
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111827)),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                                    ) {
                                        Text("Save", color = PureWhite, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    if (toastMessage.isNotEmpty()) {
                        Text(
                            text = toastMessage,
                            color = Color(0xFF111827),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Button(
                        onClick = onVerified,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111827)),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("verify_confirm_btn")
                    ) {
                        Text("Simulate Verification & Proceed", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }

                    TextButton(
                        onClick = {
                            toastMessage = "New clearance verification email dispatched."
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                    ) {
                        Text(
                            text = "Resend verification email",
                            color = Color(0xFF111827),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// 3. STEP 2: SELECT ORGANIZATION TYPE SCREEN
// ----------------------------------------------------
@Composable
fun OrganizationSetupScreen(
    innerPadding: PaddingValues,
    onBack: () -> Unit,
    onNext: (String) -> Unit
) {
    var selectedType by remember { mutableStateOf("") } // "Construction" or "Supplier"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .background(PureWhite, CircleShape)
                    .border(1.dp, BorderGray, CircleShape)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF111827))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Step 2: Organization Model",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = PureWhite.copy(alpha = 0.95f)
                ),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFF111827).copy(alpha = 0.15f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Select Organization Type",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF111827),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Direct matching requires categorizing your operating structure correctly.",
                            fontSize = 13.sp,
                            color = Color(0xFF111827).copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                        )
                    }

                    // Option 1: Procurement Organization
                    val isConstruction = selectedType == "Procurement"
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isConstruction) Color(0xFF111827).copy(alpha = 0.05f) else PureWhite
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(
                            width = 1.5.dp,
                            color = if (isConstruction) Color(0xFF111827) else BorderGray
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedType = "Procurement" }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = if (isConstruction) Color(0xFF111827) else BorderGray.copy(alpha = 0.3f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Procurement Symbol",
                                    tint = if (isConstruction) PureWhite else Color(0xFF111827).copy(alpha = 0.6f),
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Procurement Organization",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF111827)
                                )
                                Text(
                                    text = "Setup procurement pipelines, broadcast RFQs to suppliers, track quotation margins, and view full intelligence arrays. Default Role: Organization Admin.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF111827).copy(alpha = 0.7f),
                                    lineHeight = 15.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }

                    // Option 2: Supplier Company
                    val isSupplier = selectedType == "Supplier"
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSupplier) Color(0xFF111827).copy(alpha = 0.05f) else PureWhite
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(
                            width = 1.5.dp,
                            color = if (isSupplier) Color(0xFF111827) else BorderGray
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedType = "Supplier" }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = if (isSupplier) Color(0xFF111827) else BorderGray.copy(alpha = 0.3f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.List,
                                    contentDescription = "Supplier cargo",
                                    tint = if (isSupplier) PureWhite else Color(0xFF111827).copy(alpha = 0.6f),
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Supplier Company",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF111827)
                                )
                                Text(
                                    text = "Receive tender broadcasts, submit customized quotations, manage materials supplied, and expand operational connections. Default Role: Supplier.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF111827).copy(alpha = 0.7f),
                                    lineHeight = 15.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = { onNext(selectedType) },
                        enabled = selectedType.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF111827),
                            disabledContainerColor = Color(0xFF111827).copy(alpha = 0.25f)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text("Next: Profile Credentials (Step 3)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

// --------------------------------------------------// 4. STEP 3: SPECIFIC PROFILE SETUP SCREEN (CHEVRON SUBSTEPS SUBFLOW)
// ----------------------------------------------------
@Composable
fun ProfileSetupScreen(
    innerPadding: PaddingValues,
    orgType: String,
    onBack: () -> Unit,
    onFinishOnboarding: (String, String, String, List<UserAccount>) -> Unit
) {
    // ---------------- COMMON / PROCUREMENT STATES ----------------
    var companyName by remember { mutableStateOf("") } // SCREEN 5A & 5B: Organization Name / Company Name
    var industry by remember { mutableStateOf("") } // SCREEN 5A: Industry
    var orgSize by remember { mutableStateOf("") } // SCREEN 5A: Organization Size
    var country by remember { mutableStateOf("United States") } // SCREEN 5A & 5B: Country
    var city by remember { mutableStateOf("") } // SCREEN 5A & 5B: City

    var jobTitle by remember { mutableStateOf("") } // SCREEN 6A: Job Title
    var department by remember { mutableStateOf("") } // SCREEN 6A: Department
    var phoneNumber by remember { mutableStateOf("") } // SCREEN 6A: Phone Number

    // SCREEN 7A: Invite Team Members list
    val invitedMembers = remember { mutableStateListOf<UserAccount>() }
    var inviteName by remember { mutableStateOf("") }
    var inviteEmail by remember { mutableStateOf("") }
    var inviteRole by remember { mutableStateOf("Procurement Manager") }

    // ---------------- SUPPLIER STATES ----------------
    var supplierCategory by remember { mutableStateOf("") } // SCREEN 5B: Supplier Category
    var companyDescription by remember { mutableStateOf("") } // SCREEN 6B: Company Description
    var materialsSupplied by remember { mutableStateOf("") } // SCREEN 6B: Materials Supplied
    var deliveryRegions by remember { mutableStateOf("") } // SCREEN 6B: Delivery Regions
    var contactInfo by remember { mutableStateOf("") } // SCREEN 6B: Contact Information

    var errorMessage by remember { mutableStateOf("") }
    var currentSubStep by remember { mutableStateOf(1) } // Progresses from 1 to 3 for Procurement, 1 to 2 for Supplier

    val totalSteps = if (orgType == "Supplier") 2 else 3

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (currentSubStep > 1) {
                        currentSubStep--
                        errorMessage = ""
                    } else {
                        onBack()
                    }
                },
                modifier = Modifier
                    .size(40.dp)
                    .background(PureWhite, CircleShape)
                    .border(1.dp, BorderGray, CircleShape)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go back", tint = Color(0xFF111827))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Step 3: Onboarding Details (Stage $currentSubStep of $totalSteps)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = PureWhite.copy(alpha = 0.95f)
                ),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFF111827).copy(alpha = 0.15f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (orgType == "Supplier") {
                        // ---------------- SUPPLIER FLOW (2 STAGES) ----------------
                        if (currentSubStep == 1) {
                            // SCREEN 5B: Supplier Setup
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Supplier Setup",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF111827),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Enter your primary organization and geographic operating base.",
                                    fontSize = 13.sp,
                                    color = Color(0xFF111827).copy(alpha = 0.6f),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                                )
                            }

                            OutlinedTextField(
                                value = companyName,
                                onValueChange = { companyName = it; errorMessage = "" },
                                label = { Text("Company Name") },
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF111827),
                                    unfocusedBorderColor = BorderGray,
                                    unfocusedContainerColor = PureWhite,
                                    focusedContainerColor = PureWhite,
                                    focusedLabelColor = Color(0xFF111827),
                                    unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("supplier_comp_name")
                            )

                            OutlinedTextField(
                                value = supplierCategory,
                                onValueChange = { supplierCategory = it; errorMessage = "" },
                                label = { Text("Supplier Category (e.g. Steel, Logistics, Electrical)") },
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF111827),
                                    unfocusedBorderColor = BorderGray,
                                    unfocusedContainerColor = PureWhite,
                                    focusedContainerColor = PureWhite,
                                    focusedLabelColor = Color(0xFF111827),
                                    unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("supplier_category")
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = city,
                                    onValueChange = { city = it; errorMessage = "" },
                                    label = { Text("City") },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF111827),
                                        unfocusedBorderColor = BorderGray,
                                        unfocusedContainerColor = PureWhite,
                                        focusedContainerColor = PureWhite,
                                        focusedLabelColor = Color(0xFF111827),
                                        unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier.weight(1f).testTag("supplier_city")
                                )

                                OutlinedTextField(
                                    value = country,
                                    onValueChange = { country = it; errorMessage = "" },
                                    label = { Text("Country") },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF111827),
                                        unfocusedBorderColor = BorderGray,
                                        unfocusedContainerColor = PureWhite,
                                        focusedContainerColor = PureWhite,
                                        focusedLabelColor = Color(0xFF111827),
                                        unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier.weight(1f).testTag("supplier_country")
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    if (companyName.isEmpty() || supplierCategory.isEmpty() || city.isEmpty() || country.isEmpty()) {
                                        errorMessage = "Please fill in all requested fields to proceed."
                                        return@Button
                                    }
                                    errorMessage = ""
                                    currentSubStep = 2
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111827)),
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("supplier_step1_btn")
                            ) {
                                Text("Continue to Profile Details", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }

                        } else {
                            // SCREEN 6B: Supplier Profile Setup
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Supplier Profile Setup",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF111827),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Define material segments and contact vectors for procurement matches.",
                                    fontSize = 13.sp,
                                    color = Color(0xFF111827).copy(alpha = 0.6f),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                                )
                            }

                            OutlinedTextField(
                                value = companyDescription,
                                onValueChange = { companyDescription = it; errorMessage = "" },
                                label = { Text("Company Description") },
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF111827),
                                    unfocusedBorderColor = BorderGray,
                                    unfocusedContainerColor = PureWhite,
                                    focusedContainerColor = PureWhite,
                                    focusedLabelColor = Color(0xFF111827),
                                    unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("supplier_desc")
                            )

                            OutlinedTextField(
                                value = materialsSupplied,
                                onValueChange = { materialsSupplied = it; errorMessage = "" },
                                label = { Text("Materials Supplied (e.g. Reinforced Mesh, Fasteners)") },
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF111827),
                                    unfocusedBorderColor = BorderGray,
                                    unfocusedContainerColor = PureWhite,
                                    focusedContainerColor = PureWhite,
                                    focusedLabelColor = Color(0xFF111827),
                                    unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("supplier_materials")
                            )

                            OutlinedTextField(
                                value = deliveryRegions,
                                onValueChange = { deliveryRegions = it; errorMessage = "" },
                                label = { Text("Delivery Regions (e.g. Mid-Atlantic, Nationwide)") },
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF111827),
                                    unfocusedBorderColor = BorderGray,
                                    unfocusedContainerColor = PureWhite,
                                    focusedContainerColor = PureWhite,
                                    focusedLabelColor = Color(0xFF111827),
                                    unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("supplier_regions")
                            )

                            OutlinedTextField(
                                value = contactInfo,
                                onValueChange = { contactInfo = it; errorMessage = "" },
                                label = { Text("Contact Information Phone/Inquiries") },
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF111827),
                                    unfocusedBorderColor = BorderGray,
                                    unfocusedContainerColor = PureWhite,
                                    focusedContainerColor = PureWhite,
                                    focusedLabelColor = Color(0xFF111827),
                                    unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth().testTag("supplier_contact")
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    if (companyDescription.isEmpty() || materialsSupplied.isEmpty() || deliveryRegions.isEmpty() || contactInfo.isEmpty()) {
                                        errorMessage = "Please fill in all profile details before completing setup."
                                        return@Button
                                    }
                                    onFinishOnboarding(companyName, supplierCategory, "$city, $country ($deliveryRegions)", emptyList())
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111827)),
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("supplier_finish_btn")
                            ) {
                                Text("Complete Supplier Onboarding", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }

                    } else {
                        // ---------------- PROCUREMENT FLOW (3 STAGES) ----------------
                        if (currentSubStep == 1) {
                            // SCREEN 5A: Organization Setup
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Organization Setup",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF111827),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Setup your company account and geographic base.",
                                    fontSize = 13.sp,
                                    color = Color(0xFF111827).copy(alpha = 0.6f),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                                )
                            }

                            OutlinedTextField(
                                value = companyName,
                                onValueChange = { companyName = it; errorMessage = "" },
                                label = { Text("Organization Name") },
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF111827),
                                    unfocusedBorderColor = BorderGray,
                                    unfocusedContainerColor = PureWhite,
                                    focusedContainerColor = PureWhite,
                                    focusedLabelColor = Color(0xFF111827),
                                    unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("proc_org_name")
                            )

                            OutlinedTextField(
                                value = industry,
                                onValueChange = { industry = it; errorMessage = "" },
                                label = { Text("Industry (e.g. Civil Engineering, High-Rise)") },
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF111827),
                                    unfocusedBorderColor = BorderGray,
                                    unfocusedContainerColor = PureWhite,
                                    focusedContainerColor = PureWhite,
                                    focusedLabelColor = Color(0xFF111827),
                                    unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("proc_industry")
                            )

                            OutlinedTextField(
                                value = orgSize,
                                onValueChange = { orgSize = it; errorMessage = "" },
                                label = { Text("Organization Size (e.g. 50-250 employees)") },
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF111827),
                                    unfocusedBorderColor = BorderGray,
                                    unfocusedContainerColor = PureWhite,
                                    focusedContainerColor = PureWhite,
                                    focusedLabelColor = Color(0xFF111827),
                                    unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("proc_size")
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = city,
                                    onValueChange = { city = it; errorMessage = "" },
                                    label = { Text("City") },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF111827),
                                        unfocusedBorderColor = BorderGray,
                                        unfocusedContainerColor = PureWhite,
                                        focusedContainerColor = PureWhite,
                                        focusedLabelColor = Color(0xFF111827),
                                        unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier.weight(1f).testTag("proc_city")
                                )

                                OutlinedTextField(
                                    value = country,
                                    onValueChange = { country = it; errorMessage = "" },
                                    label = { Text("Country") },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF111827),
                                        unfocusedBorderColor = BorderGray,
                                        unfocusedContainerColor = PureWhite,
                                        focusedContainerColor = PureWhite,
                                        focusedLabelColor = Color(0xFF111827),
                                        unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier.weight(1f).testTag("proc_country")
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    if (companyName.isEmpty() || industry.isEmpty() || orgSize.isEmpty() || city.isEmpty() || country.isEmpty()) {
                                        errorMessage = "Please fill in all requested fields to proceed."
                                        return@Button
                                    }
                                    errorMessage = ""
                                    currentSubStep = 2
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111827)),
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("proc_step1_btn")
                            ) {
                                Text("Continue to Admin Profile", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }

                        } else if (currentSubStep == 2) {
                            // SCREEN 6A: Admin Profile Setup
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Admin Profile Setup",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF111827),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "As first registrant, you are automatically assigned Organization Admin roles.",
                                    fontSize = 13.sp,
                                    color = Color(0xFF111827).copy(alpha = 0.6f),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                                )
                            }

                            OutlinedTextField(
                                value = jobTitle,
                                onValueChange = { jobTitle = it; errorMessage = "" },
                                label = { Text("Job Title") },
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF111827),
                                    unfocusedBorderColor = BorderGray,
                                    unfocusedContainerColor = PureWhite,
                                    focusedContainerColor = PureWhite,
                                    focusedLabelColor = Color(0xFF111827),
                                    unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("admin_job_title")
                            )

                            OutlinedTextField(
                                value = department,
                                onValueChange = { department = it; errorMessage = "" },
                                label = { Text("Department (e.g. Procurement, Supply chain)") },
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF111827),
                                    unfocusedBorderColor = BorderGray,
                                    unfocusedContainerColor = PureWhite,
                                    focusedContainerColor = PureWhite,
                                    focusedLabelColor = Color(0xFF111827),
                                    unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("admin_dept")
                            )

                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it; errorMessage = "" },
                                label = { Text("Phone Number") },
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF111827),
                                    unfocusedBorderColor = BorderGray,
                                    unfocusedContainerColor = PureWhite,
                                    focusedContainerColor = PureWhite,
                                    focusedLabelColor = Color(0xFF111827),
                                    unfocusedLabelColor = Color(0xFF111827).copy(alpha = 0.5f)
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth().testTag("admin_phone")
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    if (jobTitle.isEmpty() || department.isEmpty() || phoneNumber.isEmpty()) {
                                        errorMessage = "Please fill in all professional fields to proceed."
                                        return@Button
                                    }
                                    errorMessage = ""
                                    currentSubStep = 3
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111827)),
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("proc_step2_btn")
                            ) {
                                Text("Continue to Invite Team", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }

                        } else {
                            // SCREEN 7A: Invite Team Members
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Invite Team Members",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF111827),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Add Procurement Managers and Executives to your company's space. (Optional)",
                                    fontSize = 13.sp,
                                    color = Color(0xFF111827).copy(alpha = 0.6f),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                                )
                            }

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF111827).copy(alpha = 0.03f)
                                ),
                                border = BorderStroke(1.dp, Color(0xFF111827).copy(alpha = 0.08f)),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Add Team Member Rosters",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color(0xFF111827)
                                    )

                                    OutlinedTextField(
                                        value = inviteName,
                                        onValueChange = { inviteName = it },
                                        label = { Text("Name") },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFF111827),
                                            unfocusedBorderColor = BorderGray
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    OutlinedTextField(
                                        value = inviteEmail,
                                        onValueChange = { inviteEmail = it },
                                        label = { Text("Email Address") },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFF111827),
                                            unfocusedBorderColor = BorderGray
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    // Role toggle
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                                    ) {
                                        Text(text = "Assign Role:", fontSize = 12.sp, color = Color(0xFF111827), fontWeight = FontWeight.Bold)
                                        
                                        val isPM = inviteRole == "Procurement Manager"
                                        Button(
                                            onClick = { inviteRole = "Procurement Manager" },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (isPM) Color(0xFF111827) else BorderGray,
                                                contentColor = if (isPM) PureWhite else Color(0xFF111827)
                                            ),
                                            shape = RoundedCornerShape(18.dp),
                                            modifier = Modifier.weight(1f).heightIn(min = 36.dp)
                                        ) {
                                            Text("Manager", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = { inviteRole = "Executive" },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (!isPM) Color(0xFF111827) else BorderGray,
                                                contentColor = if (!isPM) PureWhite else Color(0xFF111827)
                                            ),
                                            shape = RoundedCornerShape(18.dp),
                                            modifier = Modifier.weight(1f).heightIn(min = 36.dp)
                                        ) {
                                            Text("Executive", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            if (inviteName.isNotEmpty() && inviteEmail.isNotEmpty()) {
                                                invitedMembers.add(
                                                    UserAccount(
                                                        name = inviteName,
                                                        email = inviteEmail,
                                                        role = inviteRole,
                                                        companyName = companyName,
                                                        department = if (inviteRole == "Executive") "Executive Advisory" else "Procurement Operations"
                                                    )
                                                )
                                                // Clear fields for next member
                                                inviteName = ""
                                                inviteEmail = ""
                                            }
                                        },
                                        enabled = inviteName.isNotEmpty() && inviteEmail.isNotEmpty(),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111827)),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.align(Alignment.End).heightIn(min = 40.dp)
                                    ) {
                                        Text("+ Add to Workspace List", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            // Render currently invited team list
                            if (invitedMembers.isNotEmpty()) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(text = "Roster Queue:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF111827))
                                    invitedMembers.forEach { member ->
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0xFF111827).copy(alpha = 0.04f), RoundedCornerShape(8.dp))
                                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Column {
                                                Text(text = member.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF111827))
                                                Text(text = "${member.email} • ${member.role}", fontSize = 10.sp, color = TextGray)
                                            }
                                            IconButton(
                                                onClick = { invitedMembers.remove(member) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Text(
                                                    text = "✕",
                                                    color = Color.Red.copy(alpha = 0.8f),
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    onFinishOnboarding(companyName, industry, "$orgSize Employees ($city, $country)", invitedMembers.toList())
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111827)),
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("finish_onboarding_btn")
                            ) {
                                Text(
                                    text = if (invitedMembers.isEmpty()) "Skip & Complete Setup" else "Deploy Team & Complete Setup",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// 5. MASTER SECURE AUTHENTICATED ACTIVE DASHBOARD (ROLE-BASED LANDING EXPERIENCE)
// ----------------------------------------------------
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthenticatedDashboardScreen(
    innerPadding: PaddingValues,
    user: UserAccount,
    isSandbox: Boolean,
    rfqList: MutableList<DashboardItem>,
    opportunitiesList: MutableList<OpportunityItem>,
    appliedBids: MutableList<String>,
    registeredAccounts: List<UserAccount>,
    onSwitchRoleSimulated: (UserAccount) -> Unit,
    onAddPersonnel: (String, String, String, String) -> Unit,
    onLogOutClick: () -> Unit
) {
    var showAddRfqDialog by remember { mutableStateOf(false) }

    // State holders for simple forms
    var newRfqTitle by remember { mutableStateOf("") }
    var newRfqDept by remember { mutableStateOf("") }
    var newRfqBudget by remember { mutableStateOf("") }
    var newRfqTerm by remember { mutableStateOf("") }

    // Personnel user list adding state
    var showAddPersonnelDialog by remember { mutableStateOf(false) }
    var inputPName by remember { mutableStateOf("") }
    var inputPEmail by remember { mutableStateOf("") }
    var inputPRole by remember { mutableStateOf("Procurement Manager") }
    var inputPDept by remember { mutableStateOf("") }

    // Active state for supplier bidding details
    var activeBidTargetOpportunity by remember { mutableStateOf<OpportunityItem?>(null) }
    var customBidPriceCoefficient by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 22.dp)
    ) {
        // Core Platform Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(MossGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Shield Verified Ledger",
                        tint = PureWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Athena Space Desk",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MossGreenDark
                    )
                    Text(
                        text = (user.companyName.ifEmpty { "Athena Sovereignty Grid" }).uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MossGreenLight,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            IconButton(
                onClick = onLogOutClick,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MossGreen.copy(alpha = 0.08f))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Sign out clearance console",
                    tint = MossGreen
                )
            }
        }

        // Active Personnel Profile Badge Card
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MossGreen),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(PureWhite.copy(alpha = 0.2f), CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User verification",
                            tint = PureWhite,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = user.name,
                            color = PureWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .background(PureWhite.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = user.role.uppercase(),
                                    color = PureWhite,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Dept: ${user.department}",
                                color = PureWhite.copy(alpha = 0.8f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ----------------------------------------------------
        // DYNAMIC ROLE-BASED DASHBOARDS (FRONT & CENTER MANDATES)
        // ----------------------------------------------------
        when (user.role) {
            "Procurement Manager" -> {
                // FRONT & CENTER: Create RFQ Panel
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "⚡ FRONT AND CENTER: PROJECT PROCUREMENT DESK",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MossGreenDark,
                        letterSpacing = 1.sp
                    )

                    Card(
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, BorderGray),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Create RFQ Request For Quote",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SoftDark
                            )
                            Text(
                                text = "Instantly broadcast tender requirements directly matching verified suppliers.",
                                fontSize = 11.5.sp,
                                color = TextGray,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            OutlinedTextField(
                                value = newRfqTitle,
                                onValueChange = { newRfqTitle = it },
                                label = { Text("Commodity or Service Object Needed") },
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MossGreen),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().testTag("rfq_input_title")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = newRfqDept,
                                    onValueChange = { newRfqDept = it },
                                    label = { Text("Target Dept") },
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MossGreen),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f).testTag("rfq_input_dept")
                                )
                                OutlinedTextField(
                                    value = newRfqBudget,
                                    onValueChange = { newRfqBudget = it },
                                    label = { Text("Limit Budget") },
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MossGreen),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f).testTag("rfq_input_budget")
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = newRfqTerm,
                                onValueChange = { newRfqTerm = it },
                                label = { Text("Delivery Location specifications") },
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MossGreen),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().testTag("rfq_input_specs")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    if (newRfqTitle.isNotEmpty()) {
                                        val generatedId = "RFQ-2026-M" + (10..99).random()
                                        val budgetVal = if (newRfqBudget.isNotEmpty()) newRfqBudget else "$100,000"
                                        val newRfq = DashboardItem(
                                            id = generatedId,
                                            title = newRfqTitle,
                                            status = "Active",
                                            extra = if (newRfqTerm.isNotEmpty()) newRfqTerm else "Pending matches from suppliers",
                                            department = if (newRfqDept.isNotEmpty()) newRfqDept else "Structural Ops",
                                            budgetValue = budgetVal
                                        )
                                        rfqList.add(0, newRfq)

                                        // Also add it as a raw Opportunity for Suppliers to bid on!
                                        opportunitiesList.add(0, OpportunityItem(
                                            id = "OPP-" + (500..599).random(),
                                            title = newRfqTitle,
                                            orgName = user.companyName.ifEmpty { "Athena Builders Corp" },
                                            value = "$budgetVal/lot",
                                            location = newRfqTerm.ifEmpty { "Main Warehouse Hub" },
                                            category = newRfqDept.ifEmpty { "Materials" }
                                        ))

                                        // clear
                                        newRfqTitle = ""
                                        newRfqDept = ""
                                        newRfqBudget = ""
                                        newRfqTerm = ""
                                    }
                                },
                                enabled = newRfqTitle.isNotEmpty(),
                                colors = ButtonDefaults.buttonColors(containerColor = MossGreen),
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Sovereign Broadcast RFQ Target", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "📋 ACTIVE MATCHING RFQ LEDGER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MossGreenDark
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 14.dp)
                    ) {
                        items(rfqList) { item ->
                            RfqItemCard(item = item, onDelete = { rfqList.remove(item) })
                        }
                    }
                }
            }

            "Supplier" -> {
                // FRONT & CENTER: 12 New Opportunities Active Widget
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "📈 FRONT AND CENTER: CHANNELS OPPORTUNITIES LEDGER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GoldOpportunity,
                        letterSpacing = 1.sp
                    )

                    // Outstanding Opportunities Summary Banner
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MossGreen),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "12 New Opportunities",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = PureWhite
                                )
                                Text(
                                    text = "Dynamic index matching active coefficients",
                                    fontSize = 11.sp,
                                    color = PureWhite.copy(alpha = 0.8f)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .background(PureWhite.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "74.8% WIN COEFF",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = PureWhite
                                )
                            }
                        }
                    }

                    // Submission Dialog
                    if (activeBidTargetOpportunity != null) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Lavender),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, MossGreen.copy(alpha = 0.2f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Submit Secure Bid for ${activeBidTargetOpportunity!!.title}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MossGreenDark
                                )
                                Text(
                                    text = "Client Budget Target: ${activeBidTargetOpportunity!!.value}",
                                    fontSize = 11.sp,
                                    color = TextGray
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = customBidPriceCoefficient,
                                    onValueChange = { customBidPriceCoefficient = it },
                                    label = { Text("Your Bid price (e.g. $29,000)") },
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MossGreen),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(onClick = { activeBidTargetOpportunity = null }) {
                                        Text("Cancel", color = Color.Red)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = {
                                            appliedBids.add(activeBidTargetOpportunity!!.id)
                                            activeBidTargetOpportunity = null
                                            customBidPriceCoefficient = ""
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MossGreen)
                                    ) {
                                        Text("Seal Bid", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "📢 AVAILABLE ENTERPRISE TENDERS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MossGreenDark
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 14.dp)
                    ) {
                        items(opportunitiesList) { opp ->
                            val alreadyBid = appliedBids.contains(opp.id)
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = PureWhite),
                                border = BorderStroke(1.dp, BorderGray)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "ID: ${opp.id}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MossGreen)
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = if (alreadyBid) MossGreen.copy(alpha = 0.12f) else Lavender,
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = if (alreadyBid) "BID DESPATCHED" else "READY MATCH",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (alreadyBid) MossGreenDark else SoftDark
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = opp.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SoftDark)
                                    Text(text = "Issuer: ${opp.orgName}  •  Loc: ${opp.location}", fontSize = 11.5.sp, color = TextGray)
                                    Text(text = "Category: ${opp.category}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MossGreenLight)

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row {
                                            Text("Expected Budget: ", fontSize = 11.sp, color = TextGray)
                                            Text(opp.value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SoftDark)
                                        }

                                        Button(
                                            onClick = { activeBidTargetOpportunity = opp },
                                            enabled = !alreadyBid,
                                            colors = ButtonDefaults.buttonColors(containerColor = MossGreen),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text(text = if (alreadyBid) "Bidded Locked" else "Apply Bid Offer", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "Executive" -> {
                // FRONT & CENTER: $120k Saved This Quarter
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "📊 FRONT AND CENTER: EXECUTIVE SOVEREIGN METRICS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MossGreenDark,
                        letterSpacing = 1.sp
                    )

                    // Luxury savings score banner
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SoftBlueCard),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, BlueAccent.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "SAVINGS METRIC ACCELERATOR",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = BlueAccent,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = "$120,000 SAVED",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = SoftDark,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            Text(
                                text = "▲ +15.4% efficiency increase versus sovereign benchmark index targets",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MossGreenLight
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "📉 COMMODITY INDICES INFLATION TREND",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MossGreenDark
                    )

                    // Analytics performance custom canvas card
                    PerformanceAnalyticsDashboard()

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "💼 MACRO DEPARTMENT COST BENCHMARKS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MossGreenDark
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 14.dp)
                    ) {
                        item {
                            SavingsCard("Structural Steel Logistics Module", "$55,200 saved this quarter", "9 active bid responses on ledger", BlueAccent)
                        }
                        item {
                            SavingsCard("Hydraulic Portland Cement Foundational Mix", "$42,000 saved this quarter", "水泥 - Supply location optimization achieved", MossGreen)
                        }
                        item {
                            SavingsCard("Macro Freight Pipeline Overhead", "$22,800 saved this quarter", "14% shipping fee rationalized via dynamic index", GoldOpportunity)
                        }
                    }
                }
            }

            "Organization Admin" -> {
                // FRONT & CENTER: Manage Team Panel
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "🛠️ FRONT AND CENTER: DEPLOY & MANAGE WORKSPACE ROSTER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MossGreenDark,
                        letterSpacing = 1.sp
                    )

                    // Actionable buttons to deploy personnel
                    Card(
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, BorderGray),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Procurement Board Team (Roster)",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftDark
                                )

                                Button(
                                    onClick = { showAddPersonnelDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = MossGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add personnel", modifier = Modifier.size(14.dp), tint = PureWhite)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Add user", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Text(
                                text = "Administer access tags, select clearance parameters, and swap workspace simulated logs using instant simulation anchors.",
                                fontSize = 11.sp,
                                color = TextGray,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    if (showAddPersonnelDialog) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Lavender),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = "Deploy New Enterprise Personnel Node", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MossGreenDark)

                                OutlinedTextField(
                                    value = inputPName,
                                    onValueChange = { inputPName = it },
                                    label = { Text("Full Name") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = inputPEmail,
                                    onValueChange = { inputPEmail = it },
                                    label = { Text("Corporate Email coordinates") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = inputPDept,
                                    onValueChange = { inputPDept = it },
                                    label = { Text("Department designation") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Role Clearance: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 6.dp))
                                    listOf("Procurement Manager", "Executive").forEach { possibleRole ->
                                        val matches = inputPRole == possibleRole
                                        Box(
                                            modifier = Modifier
                                                .padding(end = 6.dp)
                                                .background(
                                                    color = if (matches) MossGreen else BorderGray,
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                                .clickable { inputPRole = possibleRole }
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(possibleRole, fontSize = 9.sp, color = if (matches) PureWhite else SoftDark, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(onClick = { showAddPersonnelDialog = false }) {
                                        Text("Cancel", color = Color.Red, fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = {
                                            if (inputPName.isNotEmpty() && inputPEmail.isNotEmpty()) {
                                                onAddPersonnel(inputPName, inputPEmail, inputPRole, inputPDept.ifEmpty { "Engineering Ops" })
                                                inputPName = ""
                                                inputPEmail = ""
                                                inputPDept = ""
                                                showAddPersonnelDialog = false
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MossGreen)
                                    ) {
                                        Text("Deploy Node", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "👥 ACTIVE WORKSPACE ROSTER NODES (SIMULATION SWITCH)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MossGreenDark
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 14.dp)
                    ) {
                        items(registeredAccounts) { member ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = PureWhite),
                                border = BorderStroke(1.dp, BorderGray)
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = member.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SoftDark)
                                        Text(text = member.role, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MossGreenLight)
                                        Text(text = "${member.email}  •  Dept: ${member.department}", fontSize = 10.5.sp, color = TextGray)
                                    }

                                    Button(
                                        onClick = { onSwitchRoleSimulated(member) },
                                        colors = ButtonDefaults.buttonColors(containerColor = MossGreen.copy(alpha = 0.08f)),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.height(30.dp)
                                    ) {
                                        Text("Simulate Log-in", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = MossGreen)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// CORE RE-USABLE SUB COMPONENT BLOCKS
// ----------------------------------------------------
@Composable
fun RfqItemCard(item: DashboardItem, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.id,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp,
                    color = MossGreen
                )

                Box(
                    modifier = Modifier
                        .background(
                            color = when (item.status) {
                                "Active" -> MossGreen.copy(alpha = 0.12f)
                                "Matching" -> Color(0xFFFEF3C7)
                                else -> BorderGray
                            },
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.status.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (item.status) {
                            "Active" -> MossGreenDark
                            "Matching" -> GoldOpportunity
                            else -> TextGray
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = SoftDark
            )

            Text(
                text = "Target Delivery & specifications: ${item.extra}",
                fontSize = 11.5.sp,
                color = TextGray,
                modifier = Modifier.padding(top = 2.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Text("Budget: ", fontSize = 11.sp, color = TextGray)
                    Text(item.budgetValue, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SoftDark)
                    Text(" (Dept: ${item.department})", fontSize = 10.sp, color = TextGray)
                }

                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("Withdraw RFQ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun PerformanceAnalyticsDashboard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = OffWhite),
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
            ) {
                val path = Path().apply {
                    moveTo(0f, size.height * 0.8f)
                    quadraticBezierTo(size.width * 0.25f, size.height * 0.2f, size.width * 0.5f, size.height * 0.5f)
                    quadraticBezierTo(size.width * 0.75f, size.height * 0.9f, size.width, size.height * 0.1f)
                }
                drawPath(
                    path = path,
                    color = MossGreen,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 6f)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("May Q1", fontSize = 9.sp, color = TextGray)
                Text("May Q2", fontSize = 9.sp, color = TextGray)
                Text("Today Active", fontSize = 9.sp, color = TextGray, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SavingsCard(title: String, amount: String, sub: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SoftDark)
                Text(text = amount, fontSize = 11.5.sp, color = TextGray)
                Text(text = sub, fontSize = 10.sp, color = color, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SocialSmallButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(PureWhite)
            .border(1.dp, BorderGray, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = text, tint = MossGreen, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SoftDark)
    }
}

@Composable
fun DoodleBackground(modifier: Modifier, color: Color) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Ensure canvas width and height are measured properly
        if (width <= 0 || height <= 0) return@Canvas

        // Translate and draw artistic, high-contrast, hand-drawn vector elements
        
        // 1. Double loop (Infinity) at top left
        drawContext.canvas.save()
        translate(width * 0.15f, height * 0.13f) {
            val path = Path().apply {
                moveTo(-15f, 0f)
                cubicTo(-35f, -15f, -35f, 15f, -15f, 0f)
                cubicTo(5f, -15f, 5f, 15f, -15f, 0f)
            }
            drawPath(path, color, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.5f))
        }
        drawContext.canvas.restore()

        // 2. Crescent Moon at top right
        drawContext.canvas.save()
        translate(width * 0.82f, height * 0.11f) {
            val path = Path().apply {
                moveTo(0f, -18f)
                quadraticBezierTo(-20f, -10f, -10f, 18f)
                quadraticBezierTo(-30f, 0f, 0f, -18f)
            }
            drawPath(path, color, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.5f))
        }
        drawContext.canvas.restore()

        // 3. Hand-drawn cross (+) at middle-left
        drawContext.canvas.save()
        translate(width * 0.12f, height * 0.45f) {
            drawLine(color, androidx.compose.ui.geometry.Offset(-12f, -12f), androidx.compose.ui.geometry.Offset(12f, 12f), strokeWidth = 3.5f)
            drawLine(color, androidx.compose.ui.geometry.Offset(12f, -12f), androidx.compose.ui.geometry.Offset(-12f, 12f), strokeWidth = 3.5f)
        }
        drawContext.canvas.restore()

        // 4. Smile curve at bottom-left area
        drawContext.canvas.save()
        translate(width * 0.16f, height * 0.85f) {
            val path = Path().apply {
                moveTo(-18f, -6f)
                quadraticBezierTo(0f, 14f, 18f, -6f)
            }
            drawPath(path, color, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.5f))
            drawCircle(color, radius = 2.5f, center = androidx.compose.ui.geometry.Offset(-8f, -10f))
            drawCircle(color, radius = 2.5f, center = androidx.compose.ui.geometry.Offset(8f, -10f))
        }
        drawContext.canvas.restore()

        // 5. Lightbulb / Sparkle at center-right
        drawContext.canvas.save()
        translate(width * 0.88f, height * 0.48f) {
            drawCircle(color, radius = 10f, center = androidx.compose.ui.geometry.Offset(0f, 0f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.5f))
            drawLine(color, androidx.compose.ui.geometry.Offset(0f, -10f), androidx.compose.ui.geometry.Offset(0f, -22f), strokeWidth = 3.5f)
            drawLine(color, androidx.compose.ui.geometry.Offset(10f, 0f), androidx.compose.ui.geometry.Offset(20f, 0f), strokeWidth = 3.5f)
            drawLine(color, androidx.compose.ui.geometry.Offset(-10f, 0f), androidx.compose.ui.geometry.Offset(-20f, 0f), strokeWidth = 3.5f)
        }
        drawContext.canvas.restore()

        // 6. Scattered stars / asterisks
        drawContext.canvas.save()
        translate(width * 0.48f, height * 0.15f) {
            drawLine(color, androidx.compose.ui.geometry.Offset(0f, -10f), androidx.compose.ui.geometry.Offset(0f, 10f), strokeWidth = 3f)
            drawLine(color, androidx.compose.ui.geometry.Offset(-8f, -6f), androidx.compose.ui.geometry.Offset(8f, 6f), strokeWidth = 3f)
            drawLine(color, androidx.compose.ui.geometry.Offset(-8f, 6f), androidx.compose.ui.geometry.Offset(8f, -6f), strokeWidth = 3f)
        }
        drawContext.canvas.restore()

        // 7. Exclamation mark at bottom left-mid
        drawContext.canvas.save()
        translate(width * 0.32f, height * 0.72f) {
            drawLine(color, androidx.compose.ui.geometry.Offset(0f, -18f), androidx.compose.ui.geometry.Offset(0f, -4f), strokeWidth = 3.5f)
            drawCircle(color, radius = 2.5f, center = androidx.compose.ui.geometry.Offset(0f, 4f))
        }
        drawContext.canvas.restore()

        // 8. Question mark at upper left-mid
        drawContext.canvas.save()
        translate(width * 0.28f, height * 0.29f) {
            val path = Path().apply {
                moveTo(-8f, -12f)
                cubicTo(-8f, -22f, 10f, -22f, 8f, -8f)
                cubicTo(6f, -2f, 0f, 0f, 0f, 4f)
            }
            drawPath(path, color, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.5f))
            drawCircle(color, radius = 2.5f, center = androidx.compose.ui.geometry.Offset(0f, 12f))
        }
        drawContext.canvas.restore()

        // 9. Handdrawn Checkbox at bottom mid-right
        drawContext.canvas.save()
        translate(width * 0.72f, height * 0.82f) {
            val path = Path().apply {
                moveTo(-10f, -10f)
                lineTo(10f, -10f)
                lineTo(10f, 10f)
                lineTo(-10f, 10f)
                close()
                // The check symbol
                moveTo(-6f, 0f)
                lineTo(-2f, 4f)
                lineTo(6f, -4f)
            }
            drawPath(path, color, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f))
        }
        drawContext.canvas.restore()

        // 10. Handdrawn Percent symbol % at upper left
        drawContext.canvas.save()
        translate(width * 0.31f, height * 0.52f) {
            drawLine(color, androidx.compose.ui.geometry.Offset(-12f, 12f), androidx.compose.ui.geometry.Offset(12f, -12f), strokeWidth = 3f)
            drawCircle(color, radius = 3.5f, center = androidx.compose.ui.geometry.Offset(-8f, -8f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f))
            drawCircle(color, radius = 3.5f, center = androidx.compose.ui.geometry.Offset(8f, 8f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f))
        }
        drawContext.canvas.restore()

        // 11. Custom List Bullet icon at mid-right
        drawContext.canvas.save()
        translate(width * 0.76f, height * 0.31f) {
            drawCircle(color, radius = 2.5f, center = androidx.compose.ui.geometry.Offset(-10f, -10f))
            drawLine(color, androidx.compose.ui.geometry.Offset(-2f, -10f), androidx.compose.ui.geometry.Offset(14f, -10f), strokeWidth = 3f)
            drawCircle(color, radius = 2.5f, center = androidx.compose.ui.geometry.Offset(-10f, 0f))
            drawLine(color, androidx.compose.ui.geometry.Offset(-2f, 0f), androidx.compose.ui.geometry.Offset(14f, 0f), strokeWidth = 3f)
            drawCircle(color, radius = 2.5f, center = androidx.compose.ui.geometry.Offset(-10f, 10f))
            drawLine(color, androidx.compose.ui.geometry.Offset(-2f, 10f), androidx.compose.ui.geometry.Offset(14f, 10f), strokeWidth = 3f)
        }
        drawContext.canvas.restore()

        // 12. Scattered mini loops and circles to fill negative space
        drawCircle(color, radius = 5f, center = androidx.compose.ui.geometry.Offset(width * 0.88f, height * 0.28f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f))
        drawCircle(color, radius = 4f, center = androidx.compose.ui.geometry.Offset(width * 0.32f, height * 0.62f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f))
        drawCircle(color, radius = 3.5f, center = androidx.compose.ui.geometry.Offset(width * 0.12f, height * 0.29f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f))
    }
}

@Composable
fun BorderStroke(width: androidx.compose.ui.unit.Dp, color: Color): androidx.compose.foundation.BorderStroke {
    return androidx.compose.foundation.BorderStroke(width, color)
}
