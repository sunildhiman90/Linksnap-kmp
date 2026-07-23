package app.linksnap.features.profile.presentation

sealed class ProfileEvent {

    data object LoadProfile: ProfileEvent()
    data object Logout: ProfileEvent()
}