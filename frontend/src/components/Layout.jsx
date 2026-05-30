import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import {
    AppBar, Toolbar, Typography, Button, Box, Container, IconButton, Drawer, List, ListItem, ListItemButton, ListItemIcon, ListItemText, useMediaQuery, useTheme, Badge,
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import StorefrontIcon from '@mui/icons-material/Storefront';
import Inventory2Icon from '@mui/icons-material/Inventory2';
import ReceiptLongIcon from '@mui/icons-material/ReceiptLong';
import NotificationsIcon from '@mui/icons-material/Notifications';
import LogoutIcon from '@mui/icons-material/Logout';
import LoginIcon from '@mui/icons-material/Login';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import ShoppingBagIcon from '@mui/icons-material/ShoppingBag';
import { useAuth } from '../context/useAuth';
import { useNotifications } from '../context/useNotifications';

const navItems = [
    { label: 'Productos', path: '/productos', icon: <StorefrontIcon /> },
    { label: 'Inventario', path: '/inventario', icon: <Inventory2Icon /> },
    { label: 'Órdenes', path: '/ordenes', icon: <ReceiptLongIcon /> },
    { label: 'Notificaciones', path: '/notificaciones', icon: <NotificationsIcon /> },
];

export default function Layout({ children }) {
    const { isAuthenticated, logout } = useAuth();
    const { notificationCount } = useNotifications();
    const navigate = useNavigate();
    const location = useLocation();
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('md'));
    const [drawerOpen, setDrawerOpen] = useState(false);

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const drawer = isAuthenticated && (
        <Box sx={{ width: 260 }} role="presentation" onClick={() => setDrawerOpen(false)}>
            <Box sx={{ p: 2, textAlign: 'center' }}>
                <ShoppingBagIcon sx={{ fontSize: 40, color: 'primary.main', mb: 1 }} />
                <Typography variant="subtitle1" fontWeight={600}>E-Commerce</Typography>
            </Box>
            <List>
                {navItems.map((item) => (
                    <ListItem key={item.path} disablePadding>
                        <ListItemButton
                            selected={location.pathname === item.path}
                            onClick={() => navigate(item.path)}
                            sx={{
                                mx: 1, borderRadius: 3, mb: 0.5,
                                '&.Mui-selected': { bgcolor: 'primary.light', color: 'primary.dark', '& .MuiListItemIcon-root': { color: 'primary.dark' } },
                            }}
                        >
                            <ListItemIcon sx={{ minWidth: 40 }}>{item.icon}</ListItemIcon>
                            <ListItemText primary={item.label} />
                        </ListItemButton>
                    </ListItem>
                ))}
            </List>
        </Box>
    );

    return (
        <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh', bgcolor: 'background.default' }}>
            <AppBar position="sticky" elevation={0} sx={{ bgcolor: 'white', borderBottom: '1px solid', borderColor: 'divider' }}>
                <Toolbar sx={{ gap: 1 }}>
                    {isMobile && isAuthenticated && (
                        <IconButton edge="start" onClick={() => setDrawerOpen(true)} sx={{ color: 'primary.main' }}>
                            <MenuIcon />
                        </IconButton>
                    )}
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mr: isAuthenticated ? 2 : 0 }}>
                        <ShoppingBagIcon sx={{ color: 'primary.main', fontSize: 28 }} />
                        <Typography variant="h6" sx={{ color: 'primary.main', fontWeight: 700, letterSpacing: '-0.02em', display: { xs: 'none', sm: 'block' } }}>
                            E-Commerce
                        </Typography>
                    </Box>
                    {!isMobile && isAuthenticated && (
                        <Box sx={{ display: 'flex', gap: 0.5, ml: 1 }}>
                            {navItems.map((item) => (
                                <Button
                                    key={item.path}
                                    startIcon={item.icon}
                                    onClick={() => navigate(item.path)}
                                    variant={location.pathname === item.path ? 'contained' : 'text'}
                                    size="small"
                                    sx={{ borderRadius: 3 }}
                                >
                                    {item.label}
                                </Button>
                            ))}
                        </Box>
                    )}
                    <Box sx={{ flexGrow: 1 }} />
                    {isAuthenticated && (
                        <IconButton onClick={() => navigate('/notificaciones')} sx={{ color: location.pathname === '/notificaciones' ? 'primary.main' : 'text.secondary', mr: 1 }}>
                            <Badge badgeContent={notificationCount} color="error" max={99}>
                                <NotificationsIcon />
                            </Badge>
                        </IconButton>
                    )}
                    {isAuthenticated ? (
                        <Button startIcon={<LogoutIcon />} onClick={handleLogout} color="error" variant="outlined" size="small" sx={{ borderRadius: 3 }}>
                            Salir
                        </Button>
                    ) : (
                        <Box sx={{ display: 'flex', gap: 1 }}>
                            <Button startIcon={<LoginIcon />} onClick={() => navigate('/login')} variant="contained" size="small">
                                Login
                            </Button>
                            <Button startIcon={<PersonAddIcon />} onClick={() => navigate('/register')} variant="outlined" size="small">
                                Registrar
                            </Button>
                        </Box>
                    )}
                </Toolbar>
            </AppBar>
            {isAuthenticated && (
                <Drawer anchor="left" open={drawerOpen} onClose={() => setDrawerOpen(false)} PaperProps={{ sx: { borderRadius: '0 20px 20px 0' } }}>
                    {drawer}
                </Drawer>
            )}
            <Container component="main" maxWidth="lg" sx={{ py: 4, flexGrow: 1 }}>
                {children}
            </Container>
        </Box>
    );
}