function isAuthenticated(auth) {
    // console.log(auth);

    return Object.keys(auth).length > 0;
}

export default isAuthenticated;