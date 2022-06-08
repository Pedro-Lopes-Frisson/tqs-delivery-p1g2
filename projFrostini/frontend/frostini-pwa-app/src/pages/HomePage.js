import { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthContext from '../context/AuthProvider';
import isAuthenticated from '../utils/Authentication';
import "./HomePage.css";
import { 
    AppBar, 
    Box, 
    Toolbar, 
    IconButton, 
    Typography,
    Menu, 
    Container, 
    Button, 
    MenuItem,
    Divider,
    Grid
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import AdbIcon from '@mui/icons-material/Adb';
import banner from '../assets/banner.jpg';

/* const pages = ['Products', 'Pricing', 'Blog']; */
const user = ['Login', 'Register'];

function HomePage() {
    const { auth } = useContext(AuthContext);
    const isAuth = isAuthenticated(auth);
    const [anchorElNav, setAnchorElNav] = useState(null);
    const navigate = useNavigate();

    const handleOpenNavMenu = (event) => {
        setAnchorElNav(event.currentTarget);
    };

    const handleCloseNavMenu = (event) => {
        navigate('/' + (event.currentTarget.innerText).toLowerCase());
        setAnchorElNav(null);
    };

    useEffect(() => {
        console.log("HERE");
        //console.log(auth);
        if(isAuth) {
          navigate('/menu');
        }
      });

    return (<>
        <AppBar position="fixed" sx={{ my: 1, bgcolor: '#ffffff'}}>
            <Container maxWidth="xl" sx={{ bgcolor: '#ffffff' }}>
                <Toolbar disableGutters>
                <AdbIcon sx={{ display: { xs: 'none', md: 'flex', color: '#9cb75c' }, mr: 1 }} />
                <Typography
                    variant="h6"
                    noWrap
                    component="a"
                    href="/"
                    sx={{
                        mr: 2,
                        display: { xs: 'none', md: 'flex' },
                        fontFamily: 'monospace',
                        fontWeight: 700,
                        letterSpacing: '.3rem',
                        color: '#9cb75c',
                        textDecoration: 'none'
                    }}
                >
                    FROSTINI
                </Typography>

                <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
                    <IconButton
                        size="large"
                        aria-label="account of current user"
                        aria-controls="menu-appbar"
                        aria-haspopup="true"
                        onClick={handleOpenNavMenu}
                        color="inherit"
                    >
                    <MenuIcon sx={{ color: '#9cb75c' }} />
                    </IconButton>
                    <Menu
                        id="menu-appbar"
                        anchorEl={anchorElNav}
                        anchorOrigin={{
                            vertical: 'bottom',
                            horizontal: 'left',
                        }}
                        keepMounted
                        transformOrigin={{
                            vertical: 'top',
                            horizontal: 'left',
                        }}
                        open={Boolean(anchorElNav)}
                        onClose={handleCloseNavMenu}
                        sx={{
                            display: { xs: 'block', md: 'none', color: '#f8c8b9' },
                        }}
                    >
                    {/* {pages.map((page) => (
                        <MenuItem key={page} onClick={handleCloseNavMenu} className="pages-btn">
                        <Typography textAlign="center">{page}</Typography>
                        </MenuItem>
                    ))}

                    <Divider variant="middle" /> */}

                    {user.map((page) => (
                        <MenuItem key={page} onClick={handleCloseNavMenu} className="user-btn">
                        <Typography textAlign="center">{page}</Typography>
                        </MenuItem>
                    ))}
                    </Menu>
                </Box>
                <AdbIcon sx={{ display: { xs: 'flex', md: 'none' }, mr: 1 }} />
                <Typography
                    variant="h5"
                    noWrap
                    component="a"
                    href=""
                    sx={{
                        mr: 2,
                        display: { xs: 'flex', md: 'none' },
                        flexGrow: 1,
                        fontFamily: 'monospace',
                        fontWeight: 700,
                        letterSpacing: '.3rem',
                        color: '#f8c8b9',
                        textDecoration: 'none',
                    }}
                >
                    FROSTINI
                </Typography>
                <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
                    {/* {pages.map((page) => (
                    <Button
                        key={page}
                        className="pages-btn"
                        onClick={handleCloseNavMenu}
                        sx={{ my: 2, mx: 1, bgcolor: '#f8c8b9', color: '#ffffff', display: 'block'}}
                    >
                        {page}
                    </Button>
                    ))} */}
                </Box>

                <Box sx={{ flexGrow: 0 }}>
                    <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
                        {user.map((page) => (
                        <Button
                            key={page}
                            className="user-btn"
                            onClick={handleCloseNavMenu}
                            sx={{ my: 2, mx: 1, bgcolor: '#f8c8b9', color: '#ffffff', display: 'block' }}
                        >
                            {page}
                        </Button>
                        ))}
                    </Box>
                </Box>
                </Toolbar>
            </Container>
        </AppBar>
        <Grid
            container
            direction="column"
            alignItems="center"
            justifyContent="center"
            sx={{ my: 5 }}
        >
            <img 
                src={banner}
            />
        </Grid>
        <Grid sx={{ flexGrow: 1, justifyContent: 'space-evenly', display: { xs: 'none', md: 'flex' } }} >
            {user.map((page,index) => (
                <Button
                    key={page}
                    className={`user-btn-page-${index}`}
                    onClick={handleCloseNavMenu}
                    size="large"
                    sx={{ my: 2, mx: 1, p: 2, bgcolor: '#f8c8b9', color: '#555555', display: 'block' }}
                >
                    {page}
                </Button>
                ))}
        </Grid>

        <Grid 
            sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}
            direction="column"
        >
            {user.map((page, index) => (
                <Button
                    key={page}
                    className={`user-btn-page-${index}`}
                    onClick={handleCloseNavMenu}
                    sx={{ my: 2, mx: 1, bgcolor: '#f8c8b9', color: '#555555', display: 'block' }}
                >
                    {page}
                </Button>
                ))}
        </Grid>
    </>);
}

export default HomePage;