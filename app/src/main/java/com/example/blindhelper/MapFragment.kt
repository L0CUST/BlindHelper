package com.example.blindhelper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.blindhelper.data.User
import com.example.blindhelper.databinding.FragmentMapBinding
import com.example.blindhelper.viewmodel.ObstacleViewModel
import com.example.blindhelper.viewmodel.UserViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Suppress("DEPRECATION")
class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private lateinit var binding: FragmentMapBinding
    private val model : UserViewModel by activityViewModels()
    private val obstacle : ObstacleViewModel by activityViewModels()
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    lateinit var mAuth: FirebaseAuth
    lateinit var mDbRef: DatabaseReference
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0
    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                binding.btnKakaoLogin.visibility=View.VISIBLE
            }
            else{
                binding.btnKakaoLogin.visibility=View.INVISIBLE
            }
        }
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val permissions = arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION)
        requirePermissions(permissions, 1000)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        binding.btnPlus.setOnClickListener {
            val intent = Intent(requireContext(), ObstacleActivity::class.java)
            intent.putExtra("uid", model.users.value?.uId!!)
            intent.putExtra("point", model.users.value?.point)
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            startActivity(intent)
        }
        binding.btnKakaoLogin.setOnClickListener {
            kakaoLogin()
        }
    }

    private fun startProcess() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun requirePermissions(permissions: Array<String>, requestCode: Int) {
        val isAllPermissionsGranted = permissions.all { checkSelfPermission(activity as MainActivity, it) == PermissionChecker.PERMISSION_GRANTED }
        if (isAllPermissionsGranted) {
            permissionGranted(requestCode)
        } else {
            ActivityCompat.requestPermissions(activity as MainActivity, permissions, requestCode)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            permissionGranted(requestCode)
        } else {
            permissionDenied(requestCode)
        }
    }

    private fun permissionGranted(requestCode: Int) {
        startProcess()
    }

    private fun permissionDenied(requestCode: Int) {
        Toast.makeText(activity as MainActivity,"위치 권한 승인이 필요합니다.", Toast.LENGTH_LONG).show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity as MainActivity)
        updateLocation()
    }

    override fun onInfoWindowClick(marker: Marker) {
        val intent = Intent(requireContext(), ObstacleDelActivity::class.java)
        intent.putExtra("content", marker.title)
        intent.putExtra("uuid", marker.snippet)
        startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    fun updateLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.let {
                    for(location in it.locations) {
                        Log.d("Location", "${location.latitude} , ${location.longitude}")
                        setLocation(location)
                    }
                }
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    fun setLocation(lastLocation: Location) {
        val latlng = LatLng(lastLocation.latitude, lastLocation.longitude)
        latitude=lastLocation.latitude
        longitude=lastLocation.longitude
        val markerOptions = MarkerOptions()
            .position(latlng)
            .title("현위치")
        val cameraPosition = CameraPosition.Builder()
            .target(latlng)
            .zoom(20.0f)
            .build()
        mMap.clear()
        mMap.addMarker(markerOptions)
        mMap.addCircle(CircleOptions()
            .center(latlng)
            .radius(3.0)
            .strokeColor(Color.RED)
            .fillColor(Color.TRANSPARENT)
        )

        val data = obstacle.obstacles.value
        if (data != null) {
            for (dat in data) {
                val obstaclePos = LatLng(dat.latitude, dat.longitude)
                val mMarkerOptions = MarkerOptions()
                    .position(obstaclePos)
                    .title(dat.content)
                    .snippet(dat.uuid)
                    .icon(bitmapDescriptorFromVector(activity as MainActivity, R.drawable.obstacle))
                mMap.addMarker(mMarkerOptions)

                val locA: Location = Location("")
                locA.latitude = lastLocation.latitude
                locA.longitude = lastLocation.longitude
                val locB: Location = Location("")
                locB.latitude = dat.latitude
                locB.longitude = dat.longitude
                val distance = locA.distanceTo(locB)
                if (distance <= 3) {
                    Toast.makeText(activity as MainActivity, "장애물 감지", Toast.LENGTH_SHORT).show()
                }
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        mMap.setOnInfoWindowClickListener(this@MapFragment)
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    private fun kakaoLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(ContentValues.TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                getUserInfo()
            }
        }
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                if (error != null) {
                    Log.e(ContentValues.TAG, "카카오톡으로 로그인 실패", error)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                } else if (token != null) {
                    Log.i(ContentValues.TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    getUserInfo()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
        }
    }

    private fun getUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                val kname = "${user.kakaoAccount?.profile?.nickname}"
                val kemail = "${user.kakaoAccount?.email}"
                val kid = "${user.id}"

                signUp(kname, kemail, kid, 0)
            }
        }
    }

    private fun signUp(name: String, email: String, uid: String, point:Int) {
        mAuth.createUserWithEmailAndPassword(email, uid)
            .addOnCompleteListener(MainActivity()) { task ->
                if (task.isSuccessful) {
                    addUserToDb(name, email, uid, point)
                }
                else {
                    Toast.makeText(requireContext(),"카카오 로그인 성공", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDb(name: String, email: String, uId: String, point:Int) {
        mDbRef.child("user").child(uId).setValue(User(name, email, uId, point))
        Toast.makeText(requireContext(),"카카오 로그인 성공", Toast.LENGTH_SHORT).show()
    }
    fun getPoint() {
        Toast.makeText(requireContext(), "10원이 적립되었습니다", Toast.LENGTH_SHORT).show()
    }

}